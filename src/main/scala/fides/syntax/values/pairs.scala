package fides.syntax.values

import fides.syntax.code.{Code, Expr, Ptrn, Val, ValQ, ValType, Xctr}

/**
  * A value that is made up of two values.
  */
final case class Paired[+T1 <: ValType, +T2 <: ValType]
(first: Code[Val[T1]], second: Code[Val[T2]]) extends ValQ[Paired[T1, T2]], ValType

/**
  * Pairs two values together.
  */
final case class Pair[T1 <: ValType, T2 <: ValType]
(first: Code[Expr[T1]], second: Code[Expr[T2]]) extends Expr[Paired[T1, T2]]

/**
  * Extracts the elements of a pair.
  */
final case class UnPair[T1 <: ValType, T2 <: ValType]
(first: Code[Xctr[T1]], second: Code[Xctr[T2]]) extends Xctr[Paired[T1, T2]]

/**
  * Pair pattern.
  */
final case class MatchPair[P1 <: N1, P2 <: N2, N1 <: ValType, N2 <: ValType]
(first: Code[Ptrn[P1, N1]], second: Code[Ptrn[P2, N2]]) extends Ptrn[Paired[P1, P2], Paired[N1, N2]]

// todo Try using type classes to show the four different cases, while having only one class (Pair).
