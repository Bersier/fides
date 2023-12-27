package fides2024.syntax

final case class ExtractIdentifier(key: Expr[IdentifierKey]) extends Expr[Identifier]

final case class PairTogether[P <: ([T <: ValType] =>> Polar[T]), FirstT <: ValType, SecondT <: ValType]
(first: Code[P[FirstT]], second: Code[P[SecondT]]) extends Code[P[Pair[FirstT, SecondT]]]

//final case class PairTogether2[B <: Boolean, FirstT <: ValType, SecondT <: ValType]
//(first: Code[PolarFromB[B, FirstT]], second: Code[PolarFromB[B, SecondT]]) extends Code[PolarFromB[B, Pair[FirstT, SecondT]]]

def test(): Unit =
  val first = Location[[T <: ValType] =>> Expr[T], Bool](Identifier())
//  val second = Location[[T <: ValType] =>> Expr[T], Bool](Identifier())
//  val first = PairTogether(True, True)
  val second = ExtractIdentifier(IdentifierKey())
  PairTogether[Expr, Bool, Identifier](first, second)

final case class AddElement[P <: ([T <: ValType] =>> Polar[T]), ElementT <: ValType]
(element: Code[P[ElementT]], others: Code[P[Collection[ElementT]]]) extends Code[P[Collection[ElementT]]]

//// todo for later, since it uses Integer
//final case class Observe[P <: ([T <: ValType] =>> Polar[T]), ElementT <: ValType]
//(elementSource: Code[Location[P, ElementT]], size: Code[P[Nothing]]) extends Code[P[Collection[ElementT]]]

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

/**
  * Wrapps a given value into quotes.
  *
  * Dually, when used in a negative polarity position, evaluates the given QuoteVal.
  */
final case class WrapInQuotes[P <: ([U <: ValType] =>> Polar[U]), T <: ValType]
(value: Code[P[T]]) extends Code[WrapInQuotesCodeType[P, T]]

// todo delete
// final case class Eval[T <: ValType](quotation: Code[Expr[Quotation[Expr[T]]]]) extends Expr[Val[T]]

type WrapInQuotesCodeType[P <: ([U <: ValType] =>> Polar[U]), T <: ValType] <: CodeType = P[T] match
  case Expr[T] => Expr[Quotation[Val[T]]]
  case Ptrn[T] => Ptrn[Quotation[Expr[T]]]

/**
  * Receives on @id. Reduces to the received val after reception.
  *
  * Dually, when used in a negative polarity position, emits on @id once it has a value.
  */
final case class Location[P <: ([U <: ValType] =>> Polar[U]), T <: ValType]
(id: Val[Identifier]) extends Code[P[T]] // todo , CodeType, Code[Location[P, T]]
