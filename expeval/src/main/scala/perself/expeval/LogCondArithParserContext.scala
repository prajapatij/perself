package perself.expeval

import java.util.Properties
import scala.collection.mutable.HashMap
import perself.expeval.LogCondArithParserContext._
import perself.expeval.parsers._

class LogCondArithParserContext(config: LogCondArithConfig, data: LogCondArithData) extends LogCondArithParser {
  def this() = {
    this( new LogCondArithConfig( new Properties ),
          new LogCondArithData( HashMap.empty[String, Any] ) )
  }
  
  override def getConfig: LogCondArithParserContext.LogCondArithConfig = this.config 
  override def getData: LogCondArithParserContext.LogCondArithData = this.data
  
  override def parseAndGet(exprStr: String): Option[Boolean] = {
    this.errorMsg = ""
    parseAll(evalExpr, exprStr) match {
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

object LogCondArithParserContext {    
  class LogCondArithConfig(var props: Properties) extends ParserContext.ParserConfig {
    type T = ArithParser
	override def getExtraParser: ArithParser = new ArithParserContext
  }
  
  class LogCondArithData(var dataMap: HashMap[String, Any]) extends ParserContext.ParserData {    
    override def getValue(key: String): Any = {
      return this.dataMap.getOrElse(key, None);
    }
  }
}