package fides.syntax.meta

import fides.syntax.types.*

/**
  * Wraps a value into a Quoted.
  */
final case class Wrap[
  T <: TopT,
  S <: Expr2[T], Q <: TopQ,
  +C <: ConsC[S, Q],
](value: C) extends ConsC[WrapS[T, S], Q]

/**
  * Wraps a value into a Quoted, but raises all partial escapes by one,
  * so they don't sink, but rather stay at the same depth.
  */
final case class WrapLight[T <: TopT, Q <: TopQ](value: ConsC[Expr2[T], Q]) extends ConsC[Expr2[QuoteT[Ntrl2[T]]], Q]
// todo. And do we really want this?

/**
  * Evaluates a quoted expression.
  */
final case class Eval[T <: TopT, Q <: TopQ](value: ConsC[Expr2[QuoteT[Expr2[T]]], Q]) extends ConsC[Expr2[T], Q]
