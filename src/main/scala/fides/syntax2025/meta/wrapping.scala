package fides.syntax2025.meta

import fides.syntax2025.machinery.*

/**
  * Wraps a value into a Quoted.
  */
final case class Wrap[
  D <: TopD,
  G <: ExprHG[D],
  M <: GenHM[G],
](value: Code[M]) extends Code[WrapM[D, G, M]]

/**
  * Evaluates a quoted expression.
  */
final case class Eval[
  D <: TopD,
  G <: ExprG[QuoteD[GenM[ExprHG[D]]]],
  M <: GenHM[G],
](value: Code[M]) extends Code[EvalM[D, G, M]]
