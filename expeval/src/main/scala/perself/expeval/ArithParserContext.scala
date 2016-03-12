package perself.expeval

import java.util.Properties

import scala.collection.mutable.HashMap

import perself.expeval.ArithParserContext._
import perself.expeval.parsers._


class ArithParserContext(config: ArithConfig, data: ArithData) extends ArithParser {
  
  def this() = {
    this( new ArithConfig( new Properties ),
          new ArithData( HashMap.empty[String, Any] ) )
  }
  
  override def getConfig: ArithConfig = this.config 
  override def getData: ArithData = this.data

  override def parseAndGet(exprStr: String): Option[Float] = {
    this.errorMsg = ""
    parseAll(arithExpr, exprStr) match {
      case Success (result, _) => Option( result.toString.toFloat)
      case Failure (msg, _) => { this.errorMsg = msg; None }
      case Error (msg, _) => { this.errorMsg = msg; None }
    }
  }
  
  def setDataMap(dMap: HashMap[String, Any]) {
    this.data.dataMap = dMap
  }
  def addDataMap(dMap: HashMap[String, Any]) {
    this.data.dataMap ++= dMap
  }
  
}

object ArithParserContext {
  class ArithConfig(var props: Properties) extends ParserContext.ParserConfig {
    type T = LogicParser
	override def getExtraParser: LogicParser = new LogicParser {} 
  }
  
  class ArithData(var dataMap: HashMap[String, Any]) extends ParserContext.ParserData {    
    override def getValue(key: String): Any = {
      return this.dataMap.getOrElse(key, None);
    }
  }
}