package perself.expeval.parsers

import scala.util.parsing.combinator.JavaTokenParsers

trait LogicParser extends JavaTokenParsers { 
  /**
   * 
    <expression>::=<term>{<or><term>}
	<term>::=<factor>{<and><factor>}
	<factor>::=<constant>|<not><factor>|(<expression>)
	<constant>::= false|true
	<or>::='|'
	<and>::='&'
	<not>::='!'
   * 
   */
  val or = "||"
  val and = "&&"
  val not = "!"
  
  def logic: Parser[Boolean] = 
    term~rep(or~term) ^^{
    case x~y =>
      var t = x
      y foreach (z => t = t || z._2)
      t
  }
  
  def term: Parser[Boolean] = 
    factor~rep(and~factor) ^^ {
    case x~y =>
      var t = x
      y foreach( z => t = t && z._2)
      t
  }
  
  def factor: Parser[Boolean] =
    constant | negfactor | "("~>logic<~")" ^^ {
      _.toString.toBoolean
    }
    
  def negfactor: Parser[Boolean] = 
    not~>factor ^^ {
      !_.toString.toBoolean
    }
    
  def constant: Parser[Boolean] =
    ("true" | "false" ) ^^ {
      _.toString.toBoolean
    }
  
}