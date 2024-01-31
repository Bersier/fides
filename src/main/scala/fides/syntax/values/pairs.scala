package fides.syntax.values

import fides.syntax.code.{Code, CodeTC, Expr, Polar, Ptrn, Val, ValType, Xctr}

final class PairC[V1 <: ValType, V2 <: ValType](
  one: Polar[V1],
  two: Polar[V2],
)(using CodeTC[PairC[V1, V2]{ type T1 = one.type; type T2 = two.type }, ?]) extends Polar[Paired[V1, V2]]:
  val first = one
  val second = two
  type T1 = first.type
  type T2 = second.type
end PairC

// todo how to merge variants in concrete syntax at metaprogramming level?

final case class PairD[T1, T2](one: T1, two: T2)(using CodeTC[PairD[T1, T2], ?])
// todo multiple givens; pick first that applies?

given [T1, T2, V1 <: ValType, V2 <: ValType](using
  CodeTC[T1, Val[V1]],
  CodeTC[T2, Val[V2]],
): CodeTC[PairD[T1, T2], Val[Paired[V1, V2]]]()

sealed trait Paired[+T1 <: ValType, +T2 <: ValType] extends ValType

given [One, Two, V1 <: ValType, V2 <: ValType](using
  CodeTC[One, Val[V1]],
  CodeTC[Two, Val[V2]],
): CodeTC[PairC[V1, V2]{ type T1 = One; type T2 = Two }, Val[Paired[V1, V2]]]()

given [One, Two, V1 <: ValType, V2 <: ValType](using
  CodeTC[One, Expr[V1]],
  CodeTC[Two, Expr[V2]],
): CodeTC[PairC[V1, V2]{ type T1 = One; type T2 = Two }, Expr[Paired[V1, V2]]]()

given [One, Two, V1 <: ValType, V2 <: ValType](using
  CodeTC[One, Xctr[V1]],
  CodeTC[Two, Xctr[V2]],
): CodeTC[PairC[V1, V2]{ type T1 = One; type T2 = Two }, Xctr[Paired[V1, V2]]]()

given [One, Two, P1 <: N1, P2 <: N2, N1 <: ValType, N2 <: ValType](using
  CodeTC[One, Ptrn[P1, N1]],
  CodeTC[Two, Ptrn[P2, N2]],
): CodeTC[PairC[?, ?]{ type T1 = One; type T2 = Two }, Ptrn[Paired[P1, P2], Paired[N1, N2]]]()

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
