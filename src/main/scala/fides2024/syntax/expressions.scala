package fides2024.syntax

final case class PairTogether[P <: Polarity, FirstT <: ValSup, SecondT <: ValSup]
(first: Expr[P, FirstT], second: Expr[P, SecondT]) extends Expr[P, Pair[FirstT, SecondT]]
final case class Sign[P <: Polarity, ContentsT <: ValSup]
(contents: Expr[P, ContentsT], signatory: Expr[P, IdentifierKey]) extends Expr[P, Signed[ContentsT]]
final case class Quote[P <: Polarity](code: Component) extends Expr[P, QuoteVal]
final case class AddQuotes[P <: Polarity](value: Expr[P, ValSup]) extends Expr[P, QuoteVal]

final case class Location[P <: Polarity, T <: ValSup](id: Val[Identifier]) extends Expr[P, T]
