package fides2024.syntax

/**
  * Outputs the identifier corresponding to the obtained key.
  */
final case class ExtractIdentifier(key: Expr[IdentifierKey]) extends Expr[Identifier]

/**
  * Pairs two values together.
  *
  * Dually, when P =:= Ptrn, extracts the elements of a pair.
  */
final case class Pair[P[T <: ValType] <: Polar[T], T1 <: ValType, T2 <: ValType]
(first: Code[P[T1]], second: Code[P[T2]]) extends Code[P[Paired[T1, T2]]]

final case class Unpair[T1 <: ValType, T2 <: ValType, U1 <: T1, U2 <: T2]
(first: Code[Pattern[T1, U1]], second: Code[Pattern[T2, U2]]) extends Code[Pattern[Paired[T1, T2], Paired[U1, U2]]]

/**
  * Outputs a collection with one element added to it.
  *
  * Dually, when P =:= Ptrn, (non-deterministically) extracts one element from a collection.
  */
final case class AddElement[P[U <: ValType] <: Polar[U], T <: ValType]
(element: Code[P[T]], others: Code[P[Collection[T]]]) extends Code[P[Collection[T]]]

/**
  * Waits for @size elements from @elementSource, then outputs them as a collection.
  *
  * Dually, when P =:= Ptrn, outputs the elements of a collection to @elementSource, and its size to @size.
  */
final case class Observe[P[T <: ValType] <: Polar[T], T <: ValType]
(elementSource: Code[Endpoint[P, T]], size: Code[P[Nothing]]) extends Code[P[Collection[T]]]
// todo Nothing stands for a future Integer type in Fides

/**
  * Primitive to sign messages
  *
  * Dual of Unsign
  */
final case class Sign[T <: ValType]
(contents: Code[Expr[T]], signatory: Code[Expr[IdentifierKey]]) extends Expr[Signed[T]]

/**
  * Primitive to unsign messages
  *
  * Dual of Sign
  */
final case class Unsign[T <: ValType, U <: T]
(contents: Code[Pattern[T, U]], signatory: Code[Ptrn[Identifier]]) extends Pattern[Signed[T], Signed[U]]

/**
  * Analoguous to s-Strings in Scala, but for code
  *
  * Once all the Escape inside @code have been evaluated and spliced in, reduces to a Quotation.
  *
  * Dually, can act as a code pattern.
  */
final case class Quote[P[U <: ValType] <: Polar[U], C <: CodeType](code: Code[C]) extends Code[P[Quotation[C]]]

/**
  * Wraps a value into a quotation.
  *
  * Dual of Unwrap
  */
final case class Wrap[T <: ValType](value: Code[Expr[T]]) extends Code[Expr[Quotation[Val[T]]]]

/**
  * Evaluates a quotation.
  *
  * Dual of wrap
  */
final case class Unwrap[T <: ValType, U <: T]
(value: Code[Pattern[T, U]]) extends Code[Pattern[Quotation[Expr[T]], Quotation[Expr[U]]]]

/**
  * Location end-point
  */
sealed class Endpoint[P[U <: ValType] <: Polar[U], T <: ValType]
(val id: Code[Val[Location[T]]]) extends Code[Endpoint[P, T]], CodeType
// todo instead of an id, Location(id) could be passed, that itself has a ValType

/**
  * Absorbs from the location referred to by @id. Reduces to the received val after reception.
  *
  * Synonym for Endpoint[Expr, T]
  *
  * Dual of Out.
  */
final case class Inp[T <: ValType]
(override val id: Code[Val[Location[T]]]) extends Endpoint[Expr, T](id), Expr[T], Code[Inp[T]]

/**
  * Emits to the location referred to by @id, once it has a value.
  *
  * Synonym for Endpoint[Ptrn, T]
  *
  * Dual of Inp
  */
final case class Out[T <: ValType]
(override val id: Code[Val[Location[T]]]) extends Endpoint[Ptrn, T](id), Pattern[T, Nothing], Code[Out[T]]

/**
  * Tries to match a value to the given pattern. Upon failure, outputs the value to the alternative instead.
  */
final case class Match[R <: Pattern[?, ?]](pattern: Code[R], alternative: Code[R]) extends Code[R]

/**
  * Converts a collection of component quotations to a quotation of the components, composed concurrently.
  *
  * Dually, when P =:= Ptrn, extracts the components out of a Concurrent component.
  */
final case class Zip[P[U <: ValType] <: Polar[U]]
(components: Code[P[Collection[Quotation[Component]]]]) extends Code[P[Quotation[Concurrent]]]
