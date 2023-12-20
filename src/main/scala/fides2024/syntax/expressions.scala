package fides2024.syntax

final case class PairTogether[P <: Polarity](first: Expr[P], second: Expr[P]) extends Expr[P]
final case class Sign[P <: Polarity](contents: Expr[P], signatory: Expr[P]) extends Expr[P]
final case class Quote[P <: Polarity](code: Component) extends Expr[P]
final case class AddQuotes[P <: Polarity](value: Expr[P]) extends Expr[P]

final case class Location[P <: Polarity](id: Val) extends Expr[P]
