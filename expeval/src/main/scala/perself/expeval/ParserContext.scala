package perself.expeval

import scala.util.parsing.combinator.JavaTokenParsers

trait ParserContext {  
  var errorMsg: String = _
  
  def parseAndGet(exprStr: String): Option[Any]
  
  def getConfig: ParserContext.ParserConfig
  def getData: ParserContext.ParserData
 
}

object ParserContext {
   abstract class ParserConfig{
    type T <: JavaTokenParsers
	  def getExtraParser: T
  }
  
  abstract class ParserData{
    def getValue(key: String): Any    
  }
}