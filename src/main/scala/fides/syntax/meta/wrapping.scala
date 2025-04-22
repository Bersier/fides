package fides.syntax.meta

import fides.syntax.code.Polarity.{Negative, Positive}
import fides.syntax.code.{Code, Expr, Polar, Polarity, Lit, ValTop, Xctr}

/**
  * Wraps a value into a Quoted.
  *
  * [[Wrap]]`[T] <: `[[Expr]]`[`[[Quoted]]`[`[[Lit]]`[T]]]`
  */
type Wrap[T <: ValTop] = WrapP[Positive, T, ValTop, Quoted[Lit[T]], ValTop]
object Wrap:
  inline def apply[T <: ValTop](inline value: Code[Expr[T]]): Wrap[T] = WrapP(value)
end Wrap

/**
  * Unwraps a quoted value.
  *
  * [[UnWrap]]`[T] <: `[[Xctr]]`[`[[Quoted]]`[`[[Lit]]`[T]]]`
  */
type UnWrap[T <: ValTop] = MatchWrap[Nothing, T, Nothing, Quoted[Lit[T]]]
object UnWrap:
  inline def apply[T <: ValTop](inline value: Code[Xctr[T]]): UnWrap[T] = MatchWrap(value)
end UnWrap

/**
  * Pattern for unwrapping
  */
type MatchWrap[
  P <: N,
  N <: ValTop,
  L >: Nothing <: Quoted[Lit[P]],
  U >: Quoted[Lit[N]] <: ValTop,
] = WrapP[Negative, P, N, L, U]
object MatchWrap:
  inline def apply[
    P <: N,
    N <: ValTop,
    L >: Nothing <: Quoted[Lit[P]],
    U >: Quoted[Lit[N]] <: ValTop,
  ](inline value: Code[Ptrn[P, N]])(using
    Quoted[Lit[P]] <:< (L | Quoted[Lit[Nothing]]),
    (U & Quoted[Lit[ValTop]]) <:< Quoted[Lit[N]],
  ): MatchWrap[P, N, L, U] = WrapP(value)
end MatchWrap

/**
  * General [[Polar]] for wrapping. Note that it can only be an [[Expr]] or a [[Ptrn]].
  */
final case class WrapP[
  R >: Positive & Negative <: Polarity,
  P <: N,
  N <: ValTop,
  L >: Nothing <: Quoted[Lit[P]],
  U >: Quoted[Lit[N]] <: ValTop,
](
  value: Code[Polar[R, P, N]],
)(using
  Quoted[Lit[P]] <:< (L | Quoted[Lit[Nothing]]),
  (U & Quoted[Lit[ValTop]]) <:< Quoted[Lit[N]],
) extends Polar[R, L, U]

/**
  * Evaluates a quoted expression.
  */
final case class Eval[T <: ValTop](value: Code[Expr[Quoted[Expr[T]]]]) extends Expr[T]
