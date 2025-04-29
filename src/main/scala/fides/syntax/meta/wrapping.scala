package fides.syntax.meta

import fides.syntax.core.Code
import fides.syntax.types.{Expr, Polar, Lit, ValBot, ValTop, Xctr}

/**
  * As an Expr, wraps a value into a Quoted.
  *
  * As an Xctr, unwraps a quoted value.
  */
final case class Wrap[P >: ValBot, N <: ValTop](value: Code[Polar[P, N]]) extends Code[Polar[P, N]]

/**
  * Evaluates a quoted expression.
  */
final case class Eval[T <: ValTop](value: Code[Expr[Quoted[Expr[T]]]]) extends Code[Expr[T]]
