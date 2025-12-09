package fides.syntax.meta

import fides.syntax.machinery.*

/**
  * Wraps a value into a Quoted.
  */
final case class Wrap[
  D <: TopD,
  G <: ExprHG[D], Q <: TopQ,
  M <: ConsHM[G, Q],
](value: Code[M]) extends Code[WrapM[D, G, Q, M]]

/**
  * Evaluates a quoted expression.
  */
final case class Eval[
  D <: TopD, QQ <: TopQ,
  G <: ExprG[QuoteD[ConsM[ExprHG[D], QQ]]], Q <: TopQ,
  M <: ConsHM[G, Q],
](value: Code[M]) extends Code[EvalM[D, QQ, G, Q, M]]
