package fides2024.syntax.values

import fides2024.syntax.kinds.{Code, Expr, Ptrn, Val, ValQ, ValType}

/**
  * A value that is made up of two values.
  */
final case class Paired[+T1 <: ValType, +T2 <: ValType]
(first: Code[Val[T1]], second: Code[Val[T2]]) extends ValQ[Paired[T1, T2]]

/**
  * Pairs two values together.
  */
final case class Pair[T1 <: ValType, T2 <: ValType]
(first: Code[Expr[T1]], second: Code[Expr[T2]]) extends Expr[Paired[T1, T2]]

/**
  * Extracts the elements of a pair.
  */
final case class UnPair[P1 <: N1, P2 <: N2, N1 <: ValType, N2 <: ValType]
(first: Code[Ptrn[P1, N1]], second: Code[Ptrn[P2, N2]]) extends Ptrn[Paired[P1, P2], Paired[N1, N2]]
