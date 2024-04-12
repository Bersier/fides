package fides.syntax.values

import fides.syntax.code.{Code, Expr, Ptrn, Val, ValType, Xctr}

/**
  * A value that is made up of two values.
  */
final case class Paired[+T1 <: ValType, +T2 <: ValType](
  first: Code[Val[T1]],
  second: Code[Val[T2]],
) extends Val[Paired[T1, T2]], ValType

/**
  * Pairs two values together.
  */
final case class Pair[T1 <: ValType, T2 <: ValType](
  first: Code[Expr[T1]],
  second: Code[Expr[T2]],
) extends Expr[Paired[T1, T2]]

/**
  * Extracts the elements of a pair.
  */
final case class UnPair[T1 <: ValType, T2 <: ValType](
  first: Code[Xctr[T1]],
  second: Code[Xctr[T2]],
) extends Xctr[Paired[T1, T2]]

/**
  * Pair pattern.
  *
  * @tparam L defined by the unification equation: L | Paired[Nothing, Nothing] =:= Paired[P1, P2].
  *           This equation is encoded in its bound as well as the first implicit parameter.
  * @tparam U defined by the unification equation: U & Paired[ValType, ValType] =:= Paired[N1, N2].
  *           This equation is encoded in its bound as well as the second implicit paramter.
  */
final case class MatchPair[
  P1 <: N1,
  P2 <: N2,
  N1 <: ValType,
  N2 <: ValType,
  L <: Paired[P1, P2],
  U >: Paired[N1, N2] <: ValType,
](
  first: Code[Ptrn[P1, N1]],
  second: Code[Ptrn[P2, N2]],
)(using
  Paired[P1, P2] <:< (L | Paired[Nothing, Nothing]),
  (U & Paired[ValType, ValType]) <:< Paired[N1, N2],
) extends Ptrn[L, U]
// todo we might need sealed types for the compiler to be able to provide those implicits reliably

// todo Try using type classes to show the four different cases, while having only one class (Pair).
//  (See type-class-syntax branch.)
