package fides.syntax.meta

import fides.syntax.types.*

/**
  * Wraps a value into a Quoted.
  */
final case class Wrap[
  D <: TopD,
  S <: Expr2S[D], Q <: TopQ,
  +C <: ConsC[S, Q],
](value: C) extends ConsC[WrapS[D, S], Q]

/**
  * Wraps a value into a Quoted, but raises all partial escapes by one,
  * so they don't sink, but rather stay at the same depth.
  */
final case class WrapLight[D <: TopD, Q <: TopQ](value: ConsC[Expr2S[D], Q]) extends ConsC[Expr2S[QuoteD[Ntrl2S[D]]], Q]
// todo. And do we really want this?

/**
  * Evaluates a quoted expression.
  */
final case class Eval[D <: TopD, Q <: TopQ](value: ConsC[Expr2S[QuoteD[Expr2S[D]]], Q]) extends ConsC[Expr2S[D], Q]
