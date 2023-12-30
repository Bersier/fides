package fides2024.syntax

final case class ExtractIdentifier(key: Expr[IdentifierKey]) extends Expr[Identifier]

final case class PairTogether[P[T <: ValType] <: Polar[T], T1 <: ValType, T2 <: ValType]
(first: Code[P[T1]], second: Code[P[T2]]) extends Code[P[Pair[T1, T2]]]

def test(): Unit =
  val posLoc = Inp[Bool](Identifier())
  val negLoc = Out[Bool](Identifier())
  val extractedIdentifier = ExtractIdentifier(IdentifierKey())
  println(Sign(PairTogether(posLoc, extractedIdentifier), IdentifierKey()))
  println(Sign(PairTogether(Identifier(), Identifier()), IdentifierKey()))
  println(Sign[Bool](posLoc, IdentifierKey()))
  println(Unsign[Bool](negLoc, Identifier()))
  println(Sign(Wrap(posLoc), IdentifierKey()))
  println(Unsign(Unwrap(negLoc), Identifier()))

final case class AddElement[P[U <: ValType] <: Polar[U], T <: ValType]
(element: Code[P[T]], others: Code[P[Collection[T]]]) extends Code[P[Collection[T]]]

//// todo for later, since it uses Integer
//final case class Observe[P[T <: ValType] <: Polar[T], ElementT <: ValType]
//(elementSource: Code[Location[P, ElementT]], size: Code[P[Nothing]]) extends Code[P[Collection[ElementT]]]

/**
  * Primitive to sign messages.
  *
  * Dual of Unsign.
  */
final case class Sign[T <: ValType]
(contents: Code[Expr[T]], signatory: Code[Expr[IdentifierKey]]) extends Expr[Signed[T]]

/**
  * Primitive to unsign messages.
  *
  * Dual of Sign.
  */
final case class Unsign[T <: ValType]
(contents: Code[Ptrn[T]], signatory: Code[Ptrn[Identifier]]) extends Ptrn[Signed[T]]

/**
  * Analoguous to s-Strings in Scala, but for code.
  *
  * Once all the Escape inside @code have been evaluated and spliced in, reduces to a QuoteVal.
  */
final case class Quote[P[U <: ValType] <: Polar[U], C <: CodeType](code: Code[C]) extends Code[P[Quotation[C]]]

/**
  * Wrapps a given value into quotes.
  *
  * Dual of Eval.
  */
final case class Wrap[T <: ValType](value: Code[Expr[T]]) extends Code[Expr[Quotation[Val[T]]]]

/**
  * Evaluates a quotation.
  *
  * Dual of wrap.
  */
final case class Unwrap[T <: ValType](value: Code[Ptrn[T]]) extends Code[Ptrn[Quotation[Expr[T]]]]

/**
  * Absorbs from the location referred to by @id. Reduces to the received val after reception.
  *
  * Dual of Out.
  */
final case class Inp[T <: ValType](id: Code[Val[Identifier]]) extends Expr[T]
// todo , CodeType, Code[Location[P, T]]

/**
  * Emits to the location referred to by @id, once it has a value.
  *
  * Dual of Inp.
  */
final case class Out[T <: ValType](id: Code[Val[Identifier]]) extends Ptrn[T]
