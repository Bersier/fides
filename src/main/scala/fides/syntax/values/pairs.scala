package fides.syntax.values

import fides.syntax.code.{Code, Expr, Polar, Lit, ValTop, Xctr}

/**
  * Data type for pairs
  */
sealed trait PairT[+T1 <: ValTop, +T2 <: ValTop] extends ValTop

/**
  * General [[Polar]] for pairing.
  */
final case class Pair[
  P1 >: Nothing,
  P2 >: Nothing,
  N1 <: ValTop,
  N2 <: ValTop,
](
  first: Code[Polar[P1, N1]],
  second: Code[Polar[P2, N2]],
) extends Polar[PairT[P1, P2], PairT[N1, N2]]
