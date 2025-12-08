package fides.syntax.meta

import fides.syntax.machinery.*

/**
  * Wraps a value into a Quoted.
  */
final case class Wrap[
  D <: TopD,
  G <: ExprG[D], Q <: TopQ,
  +M <: ConsM[G, Q],
](value: M) extends ConsM[WrapG[D, G], Q]

/**
  * Evaluates a quoted expression.
  */
final case class Eval[D <: TopD, Q <: TopQ](value: ConsM[ExprG[QuoteD[ExprG[D]]], Q]) extends ConsM[ExprG[D], Q]
