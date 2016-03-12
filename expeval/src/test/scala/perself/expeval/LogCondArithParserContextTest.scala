package perself.expeval

import org.scalatest.FlatSpec
import scala.collection.mutable.HashMap

class LogCondArithParserContextTest extends FlatSpec {
  it should "Not Fail" in {
    val lcapc = new LogCondArithParserContext()
    
    val dMap = dataMap
    
    lcapc.addDataMap(dMap)
    
    val exprsn = "(1+1) == 3 || ((abc+def) == (abc+def+1-1) && (var1 == \"abc\" || var2 == \"def\") )"
    val langOut = (1+1) == 3 || ((dMap("abc").toString.toFloat + dMap("def").toString.toFloat) == (dMap("abc").toString.toFloat+dMap("def").toString.toFloat+1-1) && (dMap("var1")=="abc") || dMap("var2")=="def")
    val lcapcOut = lcapc.parseAndGet(exprsn)
    
    assert(lcapcOut != None)
    assert(lcapcOut.get == langOut)
  }
  
  it should "Fail and give Error" in {
    val lcapc = new LogCondArithParserContext()
    
    lcapc.addDataMap (dataMap)
    
    val exprsn = "(1+1) == 3 || ((abc+def) == ( (abc+def+1-1) && (var1 == \"abc\"))"    
    val lcapcOut = lcapc.parseAndGet(exprsn)
    
    assert(lcapcOut == None)
    assert(lcapc.errorMsg != "")
  }
  
  it should "Fail with Exception" in {
    val lcapc = new LogCondArithParserContext()
    
    lcapc.addDataMap (dataMap)    
    val exprsn = "(1+1) == 3 || ((abc+def) == ( (abc1+def+1-1) && (var1 == \"abc\"))"
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