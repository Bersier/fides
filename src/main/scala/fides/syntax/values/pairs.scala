package fides.syntax.values

import fides.syntax.core.Code
import fides.syntax.types.{PairT, Polar, ValBot, ValTop}

/**
  * General [[Polar]] for pairing.
  */
final case class Pair[
  P1 >: ValBot,
  P2 >: ValBot,
  N1 <: ValTop,
  N2 <: ValTop,
](
  first: Code[Polar[P1, N1]],
  second: Code[Polar[P2, N2]],
) extends Code[Polar[PairT[P1, P2], PairT[N1, N2]]]
