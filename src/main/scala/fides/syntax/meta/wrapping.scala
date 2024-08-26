package fides.syntax.meta

import fides.syntax.code.{Code, Expr, Polar, Polarity, Ptrn, Val, ValType, Xctr}
import Polarity.{Negative, Positive}

/**
  * Wraps a value into a Quoted.
  */
type Wrap[T <: ValType] = WrapP[Positive, T, ValType, Quoted[Val[T]], ValType]
object Wrap:
  inline def apply[T <: ValType](inline value: Code[Expr[T]]): Wrap[T] = WrapP(value)
end Wrap

/**
  * Unwraps a quoted value.
  */
type UnWrap[T <: ValType] = MatchWrap[Nothing, T, Nothing, Quoted[Val[T]]]
object UnWrap:
  inline def apply[T <: ValType](inline value: Code[Xctr[T]]): UnWrap[T] = MatchWrap(value)
end UnWrap

/**
  * Pattern for unwrapping
  */
type MatchWrap[
  P <: N,
  N <: ValType,
  L >: Nothing <: Quoted[Val[P]],
  U >: Quoted[Val[N]] <: ValType,
] = WrapP[Negative, P, N, L, U]
object MatchWrap:
  inline def apply[
    P <: N,
    N <: ValType,
    L >: Nothing <: Quoted[Val[P]],
    U >: Quoted[Val[N]] <: ValType,
  ](inline value: Code[Ptrn[P, N]])(using
    Quoted[Val[P]] <:< (L | Quoted[Val[Nothing]]),
    (U & Quoted[Val[ValType]]) <:< Quoted[Val[N]],
  ): MatchWrap[P, N, L, U] = WrapP(value)
end MatchWrap

/**
  * General wrapping [[Polar]]
  */
final case class WrapP[
  R >: Positive & Negative <: Polarity,
  P <: N,
  N <: ValType,
  L >: Nothing <: Quoted[Val[P]],
  U >: Quoted[Val[N]] <: ValType,
](
  value: Code[Polar[R, P, N]],
)(using
  Quoted[Val[P]] <:< (L | Quoted[Val[Nothing]]),
  (U & Quoted[Val[ValType]]) <:< Quoted[Val[N]],
) extends Polar[R, L, U]

/**
  * Evaluates a quoted expression.
  */
final case class Eval[T <: ValType](value: Code[Expr[Quoted[Expr[T]]]]) extends Expr[T]
