package fides2024.syntax.meta

import fides2024.syntax.code.{Code, Expr, Ptrn, Val, ValType}

/**
  * Wraps a value into a Quoted.
  */
final case class Wrap[T <: ValType](value: Code[Expr[T]]) extends Expr[Quoted[Val[T]]]

/**
  * Evaluates a Quoted.
  */
final case class UnWrap[P <: N, N <: ValType](value: Code[Ptrn[P, N]]) extends Ptrn[Quoted[Expr[P]], Quoted[Expr[N]]]
