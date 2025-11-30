package fides.syntax.values

import fides.syntax.core.{Code, TopC}
import fides.syntax.types.{Aplr, BotT, PairS, PairT, Polar, Povr, TopT}

import scala.compiletime.ops.boolean.&&

/**
  * General [[Polar]] for pairing.
  */
final case class Pair[
  PP1 >: BotT,
  PP2 >: BotT,
  NP1 <: TopT,
  NP2 <: TopT,
  LP1 <: Boolean,
  LP2 <: Boolean,
  PN1 >: BotT,
  PN2 >: BotT,
  NN1 <: TopT,
  NN2 <: TopT,
  LN1 <: Boolean,
  LN2 <: Boolean,
  PS1 <: Polar[PP1, NP1, LP1],
  PS2 <: Polar[PP2, NP2, LP2],
  NS1 <: Polar[PN1, NN1, LN1],
  NS2 <: Polar[PN2, NN2, LN2],
  LS1 <: Boolean,
  LS2 <: Boolean,
  U1 <: TopC,
  U2 <: TopC,
](
  first: Code[PS1, NS1, LS1, U1],
  second: Code[PS2, NS2, LS2, U2],
) extends Code[
  PairS[PP1, PP2, NP1, NP2, LP1, LP2, PS1, PS2],
  PairS[PN1, PN2, NN1, NN2, LN1, LN2, NS1, NS2],
  LS1 && LS2,
  U1 | U2,
]
// todo replace by typed dictionary?

/**
  * As an Expr, the wires are sinks that collect values from the executing body, which are then output as a bundle.
  *
  * As an Xctr, the inputted bundle is unbundled along the wires, 
  * which are sources that feed values to the executing body.
  */
final case class Bundle(wires: Any, body: Code[Aplr]) extends Code[Povr[?, ?]]
// todo
// todo double-bundle extends Code[Bipo...
