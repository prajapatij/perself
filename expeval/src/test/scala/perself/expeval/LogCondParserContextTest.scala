package perself.expeval

import org.scalatest.FlatSpec
import scala.collection.mutable.HashMap

class LogCondParserContextTest extends FlatSpec {
 it should "Not Fail" in {
    val lcpc = new LogCondParserContext
    
    val dMap = dataMap
    
    lcpc.addDataMap(dMap)
    
    val exprsn = "(abc > def) && var1 == \"abc\""
    val langOut = (dMap("abc").toString.toFloat > dMap("def").toString.toFloat) && dMap("var1") == "abc"
    val lcpcOut = lcpc.parseAndGet(exprsn)
    
    assert(lcpcOut != None)
    assert(lcpcOut.get == langOut)
  }
  
  it should "Fail and give Error" in {
    val lcpc = new LogCondParserContext()
    
    lcpc.addDataMap (dataMap)
    
    val exprsn = "(abc > def) && var1 == \"abc\")"    
    val lcpcOut = lcpc.parseAndGet(exprsn)
    
    assert(lcpcOut == None)
    assert(lcpc.errorMsg != "")
  }
  
  it should "Fail with Exception" in {
    val lcapc = new LogCondParserContext()
    
    lcapc.addDataMap (dataMap)    
    val exprsn = "(abc1 > def) && var1 == \"abc\""
    intercept[NumberFormatException] {
    	lcapc.parseAndGet(exprsn)
    }
  }
  
  def dataMap(): HashMap[String, Any] = {
    val dMap = HashMap.empty[String, Any]
    dMap += ("abc" -> 1)
    dMap += ("def" -> 2)
    dMap += ("var1" -> "abc")
    dMap += ("var2" -> "def")
    dMap
  }
}