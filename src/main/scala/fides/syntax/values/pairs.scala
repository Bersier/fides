package fides.syntax.values

import fides.syntax.types.{Aplr, Code, Polar2, PairS, Polar, Povr, TopP, TopT}

/**
  * General [[Polar]] for pairing.
  */
final case class Pair[T1 <: TopT, T2 <: TopT, P1 <: TopP, P2 <: TopP](
  first: Code[Polar2[T1, P1]],
  second: Code[Polar2[T2, P2]],
) extends Code[PairS[T1, T2, P1, P2]]
// todo replace by record?

/**
  * As an Expr, the wires are sinks that collect values from the executing body, which are then output as a bundle.
  *
  * As an Xctr, the inputted bundle is unbundled along the wires, 
  * which are sources that feed values to the executing body.
  */
final case class Bundle(wires: Any, body: Code[Aplr]) extends Code[Povr[?, ?]]
// todo
// todo double-bundle extends Code[Bipo...
