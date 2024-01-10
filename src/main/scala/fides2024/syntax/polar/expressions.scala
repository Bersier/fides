package fides2024.syntax.polar

import fides2024.syntax.values.*
import fides2024.syntax.*

// todo organize definitions better

/**
  * Outputs the identifier corresponding to the inputted key.
  *
  * Equivalent to signing a dummy message, and then extracting the signature from it via pattern matching.
  */
final case class ExtractIdentifier(key: Code[Expr[IdentifierKey]]) extends Expr[Identifier]

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
(element: Code[Expr[T]], others: Code[Expr[Collected[T]]]) extends Expr[NonEmpty[T]]

/**
  * (Non-deterministically) extracts one element from a Collected.
  */
final case class UnAddElement[T <: ValType]
(element: Code[Xctr[T]], others: Code[Xctr[Collected[T]]]) extends Xctr[NonEmpty[T]]

/**
  * Waits for @size elements from @elementSource, then outputs them as a Collected.
  */
final case class Collect[T <: ValType]
(elementSource: Code[Inp[T]], size: Code[Expr[Integer]]) extends Expr[Collected[T]]

/**
  * Outputs the elements of a Collected to @elementSource, and its size to @size.
  */
final case class UnCollect[T <: ValType]
(elementSource: Code[Out[T]], size: Code[Xctr[Integer]]) extends Xctr[Collected[T]]

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
final case class Inp[T <: ValType](iD: Code[Val[Channel[T]]]) extends Expr[T], Code[Inp[T]]:
  override def toString: String = s"<${internalIDString(iD)}>"
end Inp

/**
  * Emits to the location referred to by @id, once it has a value.
  *
  * Should really be called UnInp. But, for convenience's sake, an exception to the naming convention is made.
  *
  * Dual of Inp
  */
final case class Out[T <: ValType](iD: Code[Val[Channel[T]]]) extends Xctr[T], Code[Out[T]]:
  override def toString: String = s"<|${internalIDString(iD)}|>"
end Out

/**
  * Emits to the location referred to by @id, once it has a matching value.
  *
  * Matches only if the type of val to be outputted is a subtype of @T,
  * so that, when used in a pattern,
  * whenever the value that would be passed to it does not match @T, the pattern will fail.
  */
final case class OutPtrn[T <: ValType](iD: Code[Val[Channel[T]]]) extends Ptrn[T, ValType], Code[Out[T]]:
  override def toString: String = s"<:${internalIDString(iD)}:>"
end OutPtrn

/**
  * Reads the value contained in the cell once the trigger value is available.
  */
final case class Read[T <: ValType](trigger: Code[Expr[U.type]], iD: Code[Val[Cell[T]]]) extends Expr[T]:
  // todo only when trigger is trivial: override def toString: String = s"[${internalIDString(iD)}]"
end Read

/**
  * Unconditionally overwrites the value contained in the cell, and signals completion.
  */
final case class Write[T <: ValType](signal: Code[Xctr[U.type]], iD: Code[Val[Cell[T]]]) extends Xctr[T]:
  // todo only when trigger is trivial: override def toString: String = s"[|${internalIDString(iD)}|]"
end Write

/**
  * Kind-of the dual of U.
  *
  * Sink, Forget
  */
final case class Ignore() extends Xctr[ValType]

/**
  * Atomically
  *  1. Reads the value of the cell, V.
  *  2. Compares V to @testValue.
  *  3. If they are the same, writes @newValue to the cell.
  *  4. Outputs V.
  *
  * Atomically compares the current value of the cell with the inputted pattern and,
  * only if they are the same, updates the value of the cell to the inputted new value, and
  * outputs the previous value of the cell.
  */
final case class CompareAndSwap[T <: ValType]
(testValue: Code[Expr[T]], newValue: Code[Expr[T]], iD: Code[Val[Cell[T]]]) extends Expr[T]

/**
  * Tries to match a value to the given pattern. Upon failure, outputs the value to the alternative instead.
  */
final case class Match[T <: ValType](pattern: Code[Ptrn[T, T]], alternative: Code[Xctr[T]]) extends Xctr[T]

/**
  * Converts a Collected of component quotations to a Quoted of the components, composed concurrently.
  */
final case class Zip(components: Code[Expr[Collected[Quoted[Component]]]]) extends Expr[Quoted[Concurrent]]

/**
  * Extracts the components out of a Concurrent component in the context of a refutable pattern.
  */
final case class UnZipPtrn(
  components: Code[Ptrn[Collected[Quoted[Component]], Collected[Quoted[Component]]]],
) extends Ptrn[Quoted[Concurrent], Quoted[Concurrent]]

/**
  * Extracts the components out of a Concurrent component in the context of an irrefutable pattern.
  */
final case class UnZip(components: Code[Xctr[Collected[Quoted[Component]]]]) extends Xctr[Quoted[Concurrent]]

/**
  * Applies a substitution to a Quoted.
  *
  * @param original the identifier to be replaced/renamed
  * @param replacement the identifer that is to replace the original
  * @param code the Quoted in which the replacement is to take place
  */
final case class Substitute[C <: CodeType](
  original: Code[Expr[Identifier]],
  replacement: Code[Expr[Identifier]],
  code: Code[Expr[Quoted[C]]],
) extends Expr[Quoted[C]]

/**
  * Outputs true iff its two identifiers are the same.
  */
final case class Equal(first: Code[Expr[Identifier]], second: Code[Expr[Identifier]]) extends Expr[Bool]
// todo corecursively extend?

private def internalIDString(iD: Code[Val[Identifier]]): String = iD match
  case c: Identifier => c.name
  case _             => iD.toString
