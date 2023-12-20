package fides2024.syntax

final case class PairTogether(first: Expr, second: Expr) extends Expr
final case class Sign(contents: Expr, signatory: Expr) extends Expr
final case class Quote(code: Component) extends Expr
final case class Evaluate(exprCode: Expr) extends Expr // todo isn't that simply the negative polarity of AddQuotes?
final case class AddQuotes(value: Expr) extends Expr

final case class Location(id: Identifier) extends Expr
