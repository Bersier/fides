package fides.syntax.values

import fides.syntax.core.Code
import fides.syntax.types.{BotT, PairT, Polr, TopT}

/**
  * General [[Polr]] for pairing.
  */
final case class Pair[
  P1 >: BotT,
  P2 >: BotT,
  N1 <: TopT,
  N2 <: TopT,
](
  first: Code[Polr[P1, N1]],
  second: Code[Polr[P2, N2]],
) extends Code[Polr[PairT[P1, P2], PairT[N1, N2]]]
