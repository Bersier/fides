package fides.syntax.values

import fides.syntax.core.Code
import fides.syntax.types.{BotT, PairT, Polar, TopT}

import scala.compiletime.ops.boolean.&&

/**
  * General [[Polr]] for pairing.
  */
final case class Pair[
  P1 >: BotT,
  P2 >: BotT,
  N1 <: TopT,
  N2 <: TopT,
  L1 <: Boolean,
  L2 <: Boolean,
](
  first: Code[Polar[P1, N1, L1]],
  second: Code[Polar[P2, N2, L2]],
) extends Code[Polar[PairT[P1, P2], PairT[N1, N2], L1 && L2]]
// todo replace by typed dictionary?
