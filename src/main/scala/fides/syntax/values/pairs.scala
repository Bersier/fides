package fides.syntax.values

import fides.syntax.core.Code
import fides.syntax.types.{Expr, Lit, PairT, Polar, ValTop, Xctr, OffTop, OffBot}

/**
  * General [[Polar]] for pairing.
  */
final case class Pair[
  P1 >: OffBot,
  P2 >: OffBot,
  N1 <: OffTop,
  N2 <: OffTop,
](
  first: Code[Polar[P1, N1]],
  second: Code[Polar[P2, N2]],
) extends Code[Polar[PairT[P1, P2], PairT[N1, N2]]]
