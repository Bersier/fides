package fides.syntax.meta

import fides.syntax.code.{Code, Expr, Ptrn, Val, ValType, Xctr}

/**
  * Wraps a value into a Quoted.
  */
final case class Wrap[T <: ValType](value: Code[Expr[T]]) extends Expr[Quoted[Val[T]]]

/**
  * Evaluates a Quoted.
  */
final case class UnWrap[T <: ValType](value: Code[Xctr[T]]) extends Xctr[Quoted[Expr[T]]]

/**
  * Pattern for unwrapping
  */
final case class MatchWrap[P <: N, N <: ValType](value: Code[Ptrn[P, N]]) extends Ptrn[Quoted[Val[P]], Quoted[Val[N]]]
