package fides2024.syntax.polar

import fides2024.syntax.values.*
import fides2024.syntax.*

/**
  * Outputs the identifier corresponding to the obtained key.
  */
final case class ExtractID(key: Expr[IdentifierKey]) extends Expr[Identifier]

/**
  * Pairs two values together.
  */
final case class Pair[T1 <: ValType, T2 <: ValType]
(first: Code[Expr[T1]], second: Code[Expr[T2]]) extends Expr[Paired[T1, T2]]

/**
  * Extracts the elements of a pair.
  */
final case class UnPair[L1 <: U1, L2 <: U2, U1 <: ValType, U2 <: ValType]
(first: Code[Ptrn[L1, U1]], second: Code[Ptrn[L2, U2]]) extends Ptrn[Paired[L1, L2], Paired[U1, U2]]

/**
  * Outputs a collection with one element added to it.
  */
final case class AddElement[T <: ValType]
(element: Code[Expr[T]], others: Code[Expr[Collection[T]]]) extends Expr[Collection[T]]

/**
  * (Non-deterministically) extracts one element from a collection.
  */
final case class UnAddElement[T <: ValType]
(element: Code[Xctr[T]], others: Code[Xctr[Collection[T]]]) extends Ptrn[Collection[T], Collection[T]]
// todo this pattern can fail when applied to an empty collection, that is why it doesn't extend Xctr.
//  But is that the right way to do it?

/**
  * Waits for @size elements from @elementSource, then outputs them as a collection.
  */
final case class Observe[T <: ValType]
(elementSource: Code[Inp[T]], size: Code[Expr[Nothing]]) extends Expr[Collection[T]]
// todo Nothing stands for a future Integer type in Fides

/**
  * Outputs the elements of a collection to @elementSource, and its size to @size.
  */
final case class UnObserve[T <: ValType]
(elementSource: Code[Out[T]], size: Code[Xctr[ValType]]) extends Xctr[Collection[T]]
// todo AnyVal stands for a future Integer type in Fides

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
final case class UnSign[L <: U, U <: ValType]
(contents: Code[Ptrn[L, U]], signatory: Code[Ptrn[Identifier, Identifier]]) extends Ptrn[Signed[L], Signed[U]]

/**
  * Analoguous to s-Strings in Scala, but for code
  *
  * Once all the Escape inside @code have been evaluated and spliced in, reduces to a Quotation.
  */
final case class Quote[C <: CodeType](code: Code[C]) extends Expr[Quotation[C]]

/**
  * Code extractor.
  */
//final case class UnQuote[L <: U, U <: CodeType](code: Code[C]) extends Ptrn[Quotation[C]]
// todo CodePtrn[L, U].?.

/**
  * Wraps a value into a quotation.
  *
  * Dual of Unwrap
  */
final case class Wrap[T <: ValType](value: Code[Expr[T]]) extends Expr[Quotation[Val[T]]]

/**
  * Evaluates a quotation.
  *
  * Dual of wrap
  */
final case class UnWrap[T <: ValType](value: Code[Xctr[T]]) extends Xctr[Quotation[Expr[T]]]
// todo as a pattern, it would be weird to evaluate the expr, only to throw it away
//  maybe it should be more symmetric with Wrap, i.e. extend Code[Xctr[Quotation[Val[T]]]]?
//  In that case, do we want a separate Eval? Or not?

/**
  * Absorbs from the location referred to by @id. Reduces to the received val after reception.
  *
  * Dual of Out.
  */
final case class Inp[T <: ValType](val iD: Code[Val[Channel[T]]]) extends Expr[T], Code[Inp[T]]

/**
  * Emits to the location referred to by @id, once it has a value.
  *
  * Dual of Inp
  */
final case class Out[T <: ValType](val iD: Code[Val[Channel[T]]]) extends Xctr[T], Code[Out[T]]

/**
  * Tries to match a value to the given pattern. Upon failure, outputs the value to the alternative instead.
  */
final case class Match[T <: ValType](pattern: Code[Ptrn[T, T]], alternative: Code[Xctr[T]]) extends Xctr[T]

/**
  * Converts a collection of component quotations to a quotation of the components, composed concurrently.
  */
final case class Zip(components: Code[Expr[Collection[Quotation[Component]]]]) extends Expr[Quotation[Concurrent]]

/**
  * Extracts the components out of a Concurrent component.
  */
final case class UnZip(components: Code[Ptrn[Collection[Quotation[Component]], Collection[Quotation[Component]]]])
extends Ptrn[Quotation[Concurrent], Quotation[Concurrent]]

// todo add Rename?
