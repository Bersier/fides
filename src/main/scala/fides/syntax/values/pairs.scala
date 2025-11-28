package fides.syntax.values

import fides.syntax.core.Code
import fides.syntax.types.{Aplr, BotT, PairT, Polar, Povr, TopT}

import scala.compiletime.ops.boolean.&&

/**
  * General [[Polar]] for pairing.
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

/**
  * As an Expr, the wires are sinks that collect values from the executing body, which are then output as a bundle.
  *
  * As an Xctr, the inputted bundle is unbundled along the wires, 
  * which are sources that feed values to the executing body.
  */
final case class Bundle(wires: Any, body: Code[Aplr]) extends Code[Povr[?, ?]]
// todo
