package fides2024.syntax

final case class ExtractIdentifier(key: Expr[Positive, IdentifierKey]) extends Expr[Positive, Identifier]

final case class PairTogether[P <: Polarity, FirstT <: ValSup, SecondT <: ValSup]
(first: Expr[P, FirstT], second: Expr[P, SecondT]) extends Expr[P, Pair[FirstT, SecondT]]

final case class AddElement[P <: Polarity, ElementT <: ValSup]
(element: Expr[P, ElementT], others: Expr[P, Collection[ElementT]]) extends Expr[P, Collection[ElementT]]

// todo for later, since it uses Integer
//final case class Observe[P <: Polarity, ElementT <: ValSup]
//(elementSource: Val[Location[P, ElementT]], size: Expr[P, Integer]) extends Expr[P, Collection[ElementT]]

/**
  * Primitive to sign messages.
  *
  * Dually, when polarity is negative, extracts contents of a signed message.
  * todo in that case, the type of @signatory should be Expr[Negative, Identifier]
  */
final case class Sign[P <: Polarity, ContentsT <: ValSup]
(contents: Expr[P, ContentsT], signatory: Expr[P, IdentifierKey]) extends Expr[P, Signed[ContentsT]]
// todo when P is negative, signatory should be an identifier

/**
  * Analoguous to s-Strings in Scala, but for code.
  *
  * Once all the Escape inside @code have been evaluated and spliced in, reduces to a QuoteVal.
  */
final case class Quote[P <: Polarity](code: Component) extends Expr[P, Quotation]

/**
  * Wrapps a given value into quotes.
  *
  * Dually, when used in a negative polarity position, evaluates the given QuoteVal.
  */
final case class WrapInQuotes[P <: Polarity](value: Expr[P, ValSup]) extends Expr[P, Quotation]

/**
  * Receives on @id. Reduces to the received val after reception.
  *
  * Dually, when used in a negative polarity position, emits on @id once it has a value.
  */
final case class Location[P <: Polarity, T <: ValSup](id: Val[Identifier]) extends Expr[P, T]
