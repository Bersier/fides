package fides.syntax.values

import fides.syntax.code.{Code, CodeTC, CodeType, Expr, Paired, Polar, Ptrn, ValType, Xctr}

final case class PairC[T1, T2](one: T1, two: T2)(using CodeTC[PairC[T1, T2], CodeType])
// todo multiple givens; pick first that applies?

given [T1, T2, V1 <: ValType, V2 <: ValType, P <: [V <: ValType] =>> CodeType : Polar](using
  CodeTC[T1, P[V1]],
  CodeTC[T2, P[V2]],
): CodeTC[PairC[T1, T2], P[Paired[V1, V2]]]()

given [T1, T2, P1 <: N1, P2 <: N2, N1 <: ValType, N2 <: ValType](using
  CodeTC[T1, Ptrn[P1, N1]],
  CodeTC[T2, Ptrn[P2, N2]],
): CodeTC[PairC[T1, T2], Ptrn[Paired[P1, P2], Paired[N1, N2]]]()

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
  */
final case class MatchPair[P1 <: N1, P2 <: N2, N1 <: ValType, N2 <: ValType](
  first: Code[Ptrn[P1, N1]],
  second: Code[Ptrn[P2, N2]],
) extends Ptrn[Paired[P1, P2], Paired[N1, N2]]
