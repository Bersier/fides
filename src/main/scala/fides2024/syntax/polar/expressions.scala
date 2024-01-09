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
final case class UnPair[P1 <: N1, P2 <: N2, N1 <: ValType, N2 <: ValType]
(first: Code[Ptrn[P1, N1]], second: Code[Ptrn[P2, N2]]) extends Ptrn[Paired[P1, P2], Paired[N1, N2]]

/**
  * Outputs a Collected with one element added to it.
  */
final case class AddElement[T <: ValType]
(element: Code[Expr[T]], others: Code[Expr[Collected[T]]]) extends Expr[Collected[T]]

/**
  * (Non-deterministically) extracts one element from a Collected.
  */
final case class UnAddElement[T <: ValType]
(element: Code[Xctr[T]], others: Code[Xctr[Collected[T]]]) extends Xctr[NonEmpty[T]]

/**
  * Waits for @size elements from @elementSource, then outputs them as a Collected.
  */
final case class Collect[T <: ValType]
(elementSource: Code[Inp[T]], size: Code[Expr[Nothing]]) extends Expr[Collected[T]]
// todo Nothing stands for a future Integer type in Fides

/**
  * Outputs the elements of a Collected to @elementSource, and its size to @size.
  */
final case class UnCollect[T <: ValType]
(elementSource: Code[Out[T]], size: Code[Xctr[ValType]]) extends Xctr[Collected[T]]
// todo AnyVal stands for a future Integer type in Fides

/**
  * Primitive to sign messages
  */
final case class Sign[T <: ValType]
(contents: Code[Expr[T]], signatory: Code[Expr[IdentifierKey]]) extends Expr[Signed[T]]

/**
  * Primitive to unsign messages
  */
final case class UnSign[P <: N, N <: ValType]
(contents: Code[Ptrn[P, N]], signatory: Code[Ptrn[Identifier, Identifier]]) extends Ptrn[Signed[P], Signed[N]]

/**
  * Analoguous to s-Strings in Scala, but for code
  *
  * Once all the Escape inside @code have been evaluated and spliced in, reduces to a Quoted.
  */
final case class Quote[C <: CodeType](code: Code[C]) extends Expr[Quoted[C]]

/**
  * Code extractor.
  */
final case class UnQuote[C <: CodeType](code: Code[C]) extends Code[Ptrn[Quoted[C], ValType]]

/**
  * Wraps a value into a Quoted.
  */
final case class Wrap[T <: ValType](value: Code[Expr[T]]) extends Expr[Quoted[Val[T]]]

/**
  * Evaluates a Quoted.
  */
final case class UnWrap[P <: N, N <: ValType](value: Code[Ptrn[P, N]]) extends Ptrn[Quoted[Expr[P]], Quoted[Expr[N]]]

/**
  * Absorbs from the location referred to by @id. Reduces to the received val after reception.
  *
  * Dual of Out
  */
final case class Inp[T <: ValType](val iD: Code[Val[Channel[T]]]) extends Expr[T], Code[Inp[T]]:
  override def toString: String = s"<${show(iD)}>"
end Inp

/**
  * Emits to the location referred to by @id, once it has a value.
  *
  * Should really be called UnInp. But, for convenience's sake, we make an exception to the naming convention.
  *
  * Dual of Inp
  */
final case class Out[T <: ValType](val iD: Code[Val[Channel[T]]]) extends Xctr[T], Code[Out[T]]:
  override def toString: String = s"[${show(iD)}]"
end Out
// todo in a refutable pattern position, it could actually make the pattern fail. Do we really want that?

private def show(iD: Code[Val[Channel[?]]]): String = iD match
  case c: Channel[?] => c.name
  case _             => iD.toString

/**
  * Tries to match a value to the given pattern. Upon failure, outputs the value to the alternative instead.
  */
final case class Match[T <: ValType](pattern: Code[Ptrn[T, T]], alternative: Code[Xctr[T]]) extends Xctr[T]

/**
  * Converts a Collected of component quotations to a Quoted of the components, composed concurrently.
  */
final case class Zip(components: Code[Expr[Collected[Quoted[Component]]]]) extends Expr[Quoted[Concurrent]]

/**
  * Extracts the components out of a Concurrent component.
  */
final case class UnZipPtrn(components: Code[Ptrn[Collected[Quoted[Component]], Collected[Quoted[Component]]]])
extends Ptrn[Quoted[Concurrent], Quoted[Concurrent]]
final case class UnZip(components: Code[Xctr[Collected[Quoted[Component]]]]) extends Xctr[Quoted[Concurrent]]
// todo do we really need these two cases?

// todo add Rename? Would allow to apply a renaming/replacement to a Quoted.
