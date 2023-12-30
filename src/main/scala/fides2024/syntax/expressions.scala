package fides2024.syntax

final case class ExtractIdentifier(key: Expr[IdentifierKey]) extends Expr[Identifier]

final case class PairTogether[P[T <: ValType] <: Polar[T], T1 <: ValType, T2 <: ValType]
(first: Code[P[T1]], second: Code[P[T2]]) extends Code[P[Pair[T1, T2]]]

def test(): Unit =
  val first = Location[[T <: ValType] =>> Expr[T], Bool](Identifier())
  val negLoc = Location[[T <: ValType] =>> Ptrn[T], Bool](Identifier())
//  val second = Location[[T <: ValType] =>> Expr[T], Bool](Identifier())
//  val first = PairTogether(True, True)
  val second = ExtractIdentifier(IdentifierKey())
  PairTogether[Expr, Bool, Identifier](first, second)

  Sign[Bool](first, IdentifierKey())
  Unsign[Bool](negLoc, Identifier())

final case class AddElement[P[U <: ValType] <: Polar[U], T <: ValType]
(element: Code[P[T]], others: Code[P[Collection[T]]]) extends Code[P[Collection[T]]]

//// todo for later, since it uses Integer
//final case class Observe[P[T <: ValType] <: Polar[T], ElementT <: ValType]
//(elementSource: Code[Location[P, ElementT]], size: Code[P[Nothing]]) extends Code[P[Collection[ElementT]]]

/**
  * Primitive to sign messages.
  *
  * Dual of Unsign. I split the definition in two because I couldn't type it properly in Scala otherwise.
  * todo can a function be written that selects the right one, based on polarity?
  * todo or maybe, by using a Boolean polarity indicator, a match type can reduce properly?
  */
final case class Sign[T <: ValType]
(contents: Code[Expr[T]], signatory: Code[Expr[IdentifierKey]]) extends Code[Expr[Signed[T]]]
// todo also keep track of signatory type?

/**
  * Primitive to unsign messages.
  *
  * Dual of Sign. I split the definition in two because I couldn't type it properly in Scala otherwise.
  */
final case class Unsign[T <: ValType]
(contents: Code[Ptrn[T]], signatory: Code[Ptrn[Identifier]]) extends Code[Ptrn[Signed[T]]]

//type SignatoryCodeType[P[U <: ValType] <: Polar[U], T <: ValType] <: CodeType = P[T] match
//  case Expr[T] => Expr[IdentifierKey]
//  case Ptrn[T] => Ptrn[Identifier]

/**
  * Analoguous to s-Strings in Scala, but for code.
  *
  * Once all the Escape inside @code have been evaluated and spliced in, reduces to a QuoteVal.
  */
final case class Quote[P[U <: ValType] <: Polar[U], C <: CodeType](code: Code[C]) extends Code[P[Quotation[C]]]

/**
  * Wrapps a given value into quotes.
  *
  * Dually, when used in a negative polarity position, evaluates the given QuoteVal.
  */
final case class WrapInQuotes[P[U <: ValType] <: Polar[U], T <: ValType]
(value: Code[P[T]]) extends Code[WrapInQuotesCodeType[P, T]]
// todo test (def probably has to be split in two)

type WrapInQuotesCodeType[P[U <: ValType] <: Polar[U], T <: ValType] <: CodeType = P[T] match
  case Expr[T] => Expr[Quotation[Val[T]]]
  case Ptrn[T] => Ptrn[Quotation[Expr[T]]]

/**
  * Receives on @id. Reduces to the received val after reception.
  *
  * Dually, when used in a negative polarity position, emits on @id once it has a value.
  */
final case class Location[P[U <: ValType] <: Polar[U], T <: ValType]
(id: Val[Identifier]) extends Code[P[T]] // todo , CodeType, Code[Location[P, T]]
