package fides2024.syntax

import fides2024.syntax.values.Quoted

/**
  * Allows escaping the body of a Quote.
  *
  * (At the top-level (outside of a quote), could represent macro code.)
  */
final case class Escape[C <: CodeType](code: Code[Expr[Quoted[C]] | Xctr[Quoted[C]]]) extends Code[C]
// todo CodePtrn...
// todo should we keep track of the polarity in the extended type?
