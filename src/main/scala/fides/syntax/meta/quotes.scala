package fides.syntax.meta

import fides.syntax.code.{Code, CodeType, Expr, Ptrn, Val, ValQ, ValType, Xctr}
import fides.syntax.values.Integer

/**
  * Code as value, used for metaprogramming
  */
final case class Quoted[+C <: CodeType](code: Code[C]) extends ValQ[Quoted[C]]

/**
  * Allows escaping the body of a Quote.
  *
  * (At the top-level (outside of a quote), could represent macro code.)
  */
final case class Escape[C <: CodeType](code: Code[Expr[Quoted[C]] | Xctr[Quoted[C]]]) extends Code[C]
// todo should we keep track of the polarity in the extended type?

/**
  * Allows matching an escape(matcher). See also SignedMatcher.
  */
final case class EscapeMatcher[C <: CodeType]
(level: Code[Val[Integer]], code: Code[Expr[Quoted[C]] | Xctr[Quoted[C]]]) extends Code[C]


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
