package fides.syntax.meta

import fides.syntax.types.{Code2, Expr2, Ntrl2, QuotedT, TopM, TopP, TopT}

/**
  * As an Expr, wraps a value into a Quoted.
  */
final case class Wrap[T <: TopT, M <: TopM](value: Code2[Expr2[T], M]) extends Code2[Expr2[QuotedT[Ntrl2[T]]], M]

/**
  * Evaluates a quoted expression.
  */
final case class Eval[T <: TopT, M <: TopM](value: Code2[Expr2[QuotedT[Expr2[T]]], M]) extends Code2[Expr2[T], M]
