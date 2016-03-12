package perself.expeval.parsers

import scala.util.parsing.combinator.JavaTokenParsers
import perself.expeval.ParserContext

trait CondParser extends JavaTokenParsers with ParserContext {
  
  def condition: Parser[Boolean] =
    cpexpr | "("~>cpexpr<~")"
  
  def cpexpr: Parser[Boolean] =
    cpliteral~("<"|">"|"=="|"!="|"<="|">=")~cpliteral ^^ {
    case a~"<"~b => a.toString.toFloat < b.toString.toFloat
    case a~">"~b => a.toString.toFloat > b.toString.toFloat
    
    case a~"=="~b =>       
      if (a.isInstanceOf[String]) a.toString.replaceAll("\"", "").equals( b.toString.replaceAll("\"", "") ) 
      else a.toString.toFloat == b.toString.toFloat
    
    case a~"!="~b =>       
      if (a.isInstanceOf[String]) !a.toString.replaceAll("\"", "").equals( b.toString.replaceAll("\"", "") ) 
      else a.toString.toFloat != b.toString.toFloat
      
    case a~">="~b => a.toString.toFloat >= b.toString.toFloat
    case a~"<="~b => a.toString.toFloat <= b.toString.toFloat
  }
  
  def cpliteral: Parser[Any] = 
    stringLiteral | floatingPointNumber | identLit 
    
  def identLit: Parser[Any] = 
    ident ^^ {    
      case x => {
        getData.getValue( x.toString )
      }
    }  
}