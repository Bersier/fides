package fides2024.syntax

final case class ExtractIdentifier(key: Expr[IdentifierKey]) extends Expr[Identifier]

final case class PairTogether[E[_] <: Polar[_], FirstT <: ValType, SecondT <: ValType]
(first: Code[E[FirstT]], second: Code[E[SecondT]]) extends Code[E[Pair[FirstT, SecondT]]]

final case class AddElement[E[_] <: Polar[_], ElementT <: ValType]
(element: Code[E[ElementT]], others: Code[E[Collection[ElementT]]]) extends Code[E[Collection[ElementT]]]

// todo for later, since it uses Integer
final case class Observe[E[_] <: Polar[_], ElementT <: ValType]
(elementSource: Code[Location[E, ElementT]], size: Code[E[Nothing]]) extends Code[E[Collection[ElementT]]]

///**
//  * Primitive to sign messages.
//  *
//  * Dually, when polarity is negative, extracts contents of a signed message.
//  * todo in that case, the type of @signatory should be Expr[Negative, Identifier]
//  */
//final case class Sign[P <: Polarity, ContentsT <: ValType]
//(contents: Expr[P, ContentsT], signatory: Expr[P, IdentifierKey]) extends Expr[P, Signed[ContentsT]]
//
///**
//  * Analoguous to s-Strings in Scala, but for code.
//  *
//  * Once all the Escape inside @code have been evaluated and spliced in, reduces to a QuoteVal.
//  */
//final case class Quote[P <: Polarity](code: Component) extends Expr[P, Quotation]
//
///**
//  * Wrapps a given value into quotes.
//  *
//  * Dually, when used in a negative polarity position, evaluates the given QuoteVal.
//  */
//final case class WrapInQuotes[P <: Polarity](value: Expr[P, ValType]) extends Expr[P, Quotation]

/**
  * Receives on @id. Reduces to the received val after reception.
  *
  * Dually, when used in a negative polarity position, emits on @id once it has a value.
  */
final case class Location[E[_] <: Polar[_], T <: ValType](id: Val[Identifier]) extends Code[Location[E, T]], CodeType
