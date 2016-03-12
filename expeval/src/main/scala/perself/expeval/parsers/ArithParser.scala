package perself.expeval.parsers

import scala.util.parsing.combinator.JavaTokenParsers
import perself.expeval.ParserContext

trait ArithParser extends JavaTokenParsers with ParserContext {
	 
	def arithExpr: Parser[Float] = 
	  arithTerm~rep("+"~arithTerm | "-"~arithTerm) ^^ {
	  case x~y => {
	    var t = x
	    y.foreach( z => z match {
	      case "+"~p => t = (t+p)
	      case "-"~p => t = (t-p)
	    })
	    t
	  }
	}
	
	def arithTerm: Parser[Float] =
	  arithFactor~rep("*"~arithFactor | "/"~arithFactor) ^^ {
	  case x~y => {
	    var t = x
	    y.foreach( z => z match {
	      case "*"~p => t = (t*p)
	      case "/"~p => t = (t/p)
	    })
	    t
	  }
	}
	
	def arithFactor: Parser[Float] =
	  ( floatingPointNumber | apIdentLit | "("~>arithExpr<~")" ) ^^ { x =>
		x.toString().toFloat  
	} 
	
	def apIdentLit: Parser[Any] = 
    ident ^^ {
      case x => getData.getValue( x.toString )
    }
}