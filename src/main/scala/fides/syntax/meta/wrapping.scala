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
  * Wraps a value into a Quoted, but raises all partial escapes by one,
  * so they don't sink, but rather stay at the same depth.
  */
final case class WrapLight[D <: TopD, Q <: TopQ](value: ConsM[ExprG[D], Q]) extends ConsM[ExprG[QuoteD[NtrlG[D]]], Q]
// todo. And do we really want this?

/**
  * Evaluates a quoted expression.
  */
final case class Eval[D <: TopD, Q <: TopQ](value: ConsM[ExprG[QuoteD[ExprG[D]]], Q]) extends ConsM[ExprG[D], Q]
