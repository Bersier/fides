package fides.syntax.meta

import fides.syntax.types.*

/**
  * Wraps a value into a Quoted.
  */
final case class Wrap[T <: TopT, M <: TopM](value: Code2[Expr2[T], M]) extends Code2[Expr2[QuotedT[Ntrl2[T]]], M]

/**
  * Wraps a value into a Quoted, but raises all partial escapes by one,
  * so they don't sink, but rather stay at the same depth.
  */
final case class WrapLight[T <: TopT, M <: TopM](value: Code2[Expr2[T], M]) extends Code2[Expr2[QuotedT[Ntrl2[T]]], M]
// todo. And do we really want this?

/**
  * Evaluates a quoted expression.
  */
final case class Eval[T <: TopT, M <: TopM](value: Code2[Expr2[QuotedT[Expr2[T]]], M]) extends Code2[Expr2[T], M]
