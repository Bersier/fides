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
// TODO when first and second are literals, Pair(first, second) should be too. So Lit and Cnst need to be revisited...
//  Although... do we really need to keep track of all literals at the type level?
