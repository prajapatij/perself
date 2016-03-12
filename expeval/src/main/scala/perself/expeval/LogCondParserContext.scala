package perself.expeval

import java.util.Properties
import scala.collection.mutable.HashMap
import perself.expeval.parsers._
import perself.expeval.LogCondParserContext._

class LogCondParserContext(config: LogCondConfig, data: LogCondData) extends LogCondParser {
  
  def this() = {
    this( new LogCondConfig( new Properties ),
          new LogCondData( HashMap.empty[String, Any] ) )
  }
  
  override def getConfig: LogCondParserContext.LogCondConfig = this.config 
  override def getData: LogCondParserContext.LogCondData = this.data
  
  override def parseAndGet(exprStr: String): Option[Boolean] = {
    this.errorMsg = ""
    parseAll(expr, exprStr) match {
      case Success (result, _) => Option( result.toString.toBoolean )
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

object LogCondParserContext {    
  class LogCondConfig(var props: Properties) extends ParserContext.ParserConfig {
    type T = ArithParser
	override def getExtraParser: ArithParser = new ArithParserContext
  }
  
  class LogCondData(var dataMap: HashMap[String, Any]) extends ParserContext.ParserData {    
    override def getValue(key: String): Any = {
      return this.dataMap.getOrElse(key, None);
    }
  }
}