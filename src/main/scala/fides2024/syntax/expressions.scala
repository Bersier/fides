package fides2024.syntax

final case class PairTogether[P <: Polarity, FirstT <: ValTop, SecondT <: ValTop]
(first: Expr[P, FirstT], second: Expr[P, SecondT]) extends Expr[P, Pair[FirstT, SecondT]]
final case class Sign[P <: Polarity, ContentsT <: ValTop]
(contents: Expr[P, ContentsT], signatory: Expr[P, IdentifierKey]) extends Expr[P, Signed[ContentsT]]
final case class Quote[P <: Polarity](code: Component) extends Expr[P, QuoteVal]
final case class AddQuotes[P <: Polarity](value: Expr[P, ValTop]) extends Expr[P, QuoteVal]

final case class Location[P <: Polarity, T <: ValTop](id: Val[Identifier]) extends Expr[P, T]
