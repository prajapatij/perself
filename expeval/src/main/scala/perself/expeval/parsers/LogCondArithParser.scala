package perself.expeval.parsers

trait LogCondArithParser extends LogCondParser with ArithParser {
  
  def evalExpr = expr

  override def cpliteral: Parser[Any] = 
    stringLiteral | floatingPointNumber | lcapLit 
    
  def lcapLit: Parser[Any] =
    identLit | "("~>cpexpr<~")" |  "("~>logic<~")" | arithExpr

}
