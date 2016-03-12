package perself.expeval.parsers

trait LogCondParser extends LogicParser with CondParser {
  
  def expr: Parser[Any] =
    logic
  
  override def constant: Parser[Boolean] =
    ("true" | "false" | condition) ^^ {
      _.toString.toBoolean
    }
  
}