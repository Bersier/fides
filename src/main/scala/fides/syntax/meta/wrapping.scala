package fides.syntax.meta

import fides.syntax.types.*

/**
  * Wraps a value into a Quoted.
  */
final case class Wrap[
  T <: TopT,
  S <: Expr2[T], M <: TopM,
  +C <: Code[S, M],
](value: C) extends Code[WrapS[T, S], M]

/**
  * Wraps a value into a Quoted, but raises all partial escapes by one,
  * so they don't sink, but rather stay at the same depth.
  */
final case class WrapLight[T <: TopT, M <: TopM](value: Code[Expr2[T], M]) extends Code[Expr2[QuoteT[Ntrl2[T]]], M]
// todo. And do we really want this?

/**
  * Evaluates a quoted expression.
  */
final case class Eval[T <: TopT, M <: TopM](value: Code[Expr2[QuoteT[Expr2[T]]], M]) extends Code[Expr2[T], M]
