package fides.syntax.values

import fides.syntax.code.{Code, Expr, Negative, Neutrale, Plrty, Polar, Positive, Ptrn, Val, ValType, Xctr}

/**
  * A value that is made up of two values.
  */
sealed trait PairT[+T1 <: ValType, +T2 <: ValType] extends ValType

/**
  * A value that is made up of two values.
  */
type Paired[T1 <: ValType, T2 <: ValType] = PairP[Neutrale, T1, T2, ValType, ValType, PairT[T1, T2], ValType]
object Paired:
  inline def apply[T1 <: ValType, T2 <: ValType](
    inline first: Code[Val[T1]],
    inline second: Code[Val[T2]],
  ): Paired[T1, T2] =
    PairP[Neutrale, T1, T2, ValType, ValType, PairT[T1, T2], ValType](first, second)
end Paired

/**
  * Pairs two values together.
  */
type Pair[T1 <: ValType, T2 <: ValType] = PairP[Positive, T1, T2, ValType, ValType, PairT[T1, T2], ValType]
object Pair:
  inline def apply[T1 <: ValType, T2 <: ValType](
    inline first: Code[Expr[T1]],
    inline second: Code[Expr[T2]],
  ): Pair[T1, T2] =
    PairP[Positive, T1, T2, ValType, ValType, PairT[T1, T2], ValType](first, second)
end Pair

/**
  * Extracts the elements of a pair.
  */
type UnPair[T1 <: ValType, T2 <: ValType] = MatchPair[Nothing, Nothing, T1, T2, Nothing, PairT[T1, T2]]
object UnPair:
  inline def apply[T1 <: ValType, T2 <: ValType](
    inline first: Code[Xctr[T1]],
    inline second: Code[Xctr[T2]],
  ): UnPair[T1, T2] =
    // See https://github.com/scala/scala3/issues/19610 as to why the type arguments need to be specified explicitly.
    MatchPair[Nothing, Nothing, T1, T2, Nothing, PairT[T1, T2]](first, second)
end UnPair

/**
  * Pair pattern.
  *
  * @tparam L defined by the unification equation: L | PairT[Nothing, Nothing] =:= PairT[P1, P2].
  *           This equation is encoded in its bound as well as the first implicit parameter.
  * @tparam U defined by the unification equation: U & PairT[ValType, ValType] =:= PairT[N1, N2].
  *           This equation is encoded in its bound as well as the second implicit paramter.
  */
type MatchPair[
  P1 <: N1,
  P2 <: N2,
  N1 <: ValType,
  N2 <: ValType,
  L >: Nothing <: PairT[P1, P2],
  U >: PairT[N1, N2] <: ValType,
] = PairP[Negative, P1, P2, N1, N2, L, U]
object MatchPair:
  inline def apply[
    P1 <: N1,
    P2 <: N2,
    N1 <: ValType,
    N2 <: ValType,
    L >: Nothing <: PairT[P1, P2],
    U >: PairT[N1, N2] <: ValType,
  ](
    inline first: Code[Ptrn[P1, N1]],
    inline second: Code[Ptrn[P2, N2]],
  )(using
    PairT[P1, P2] <:< (L | PairT[Nothing, Nothing]),
    (U & PairT[ValType, ValType]) <:< PairT[N1, N2],
  ): MatchPair[P1, P2, N1, N2, L, U] =
    PairP[Negative, P1, P2, N1, N2, L, U](first, second)
end MatchPair

final case class PairP[
  R <: Plrty,
  P1 <: N1,
  P2 <: N2,
  N1 <: ValType,
  N2 <: ValType,
  L >: Nothing <: PairT[P1, P2],
  U >: PairT[N1, N2] <: ValType,
](
  first: Code[Polar[R, P1, N1]],
  second: Code[Polar[R, P2, N2]],
)(using
  PairT[P1, P2] <:< (L | PairT[Nothing, Nothing]),
  (U & PairT[ValType, ValType]) <:< PairT[N1, N2],
) extends Polar[R, L, U]
