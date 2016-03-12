package perself.expeval

import org.scalatest.FlatSpec
import scala.collection.mutable.HashMap

class ArithParserContextTest extends FlatSpec {
  it should "Not Fail" in {
    val apc = new ArithParserContext
    
    val dMap = dataMap
    
    apc.addDataMap(dMap)
    
    val exprsn = "abc * ( def + 1 ) / 1 + 1"
    val langOut = dMap("abc").toString.toFloat * (dMap("def").toString.toFloat + 1) / 1 + 1
    val apcOut = apc.parseAndGet(exprsn)
    
    assert(apcOut != None)
    assert(apcOut.get == langOut)
  }
  
  it should "Fail and give Error" in {
    val apc = new ArithParserContext()
    
    apc.addDataMap (dataMap)
    
    val exprsn = "abc * ( def + 1 ) / 1 + 1)"    
    val apcOut = apc.parseAndGet(exprsn)
    
    assert(apcOut == None)
    assert(apc.errorMsg != "")
  }
  
  it should "Fail with Exception" in {
    val apc = new ArithParserContext()
    
    apc.addDataMap (dataMap)    
    val exprsn = "abc * ( def1 + 1 ) / 1 + 1"
    intercept[NumberFormatException] {
    	apc.parseAndGet(exprsn)
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