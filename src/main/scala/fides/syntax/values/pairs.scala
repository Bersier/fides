package fides.syntax.values

import fides.syntax.types.{Aplr, Code, Code2, TopM, Polar2, PairS, Povr, TopP, TopT}

/**
  * General [[Polar]] for pairing.
  */
final case class Pair[T1 <: TopT, T2 <: TopT, P1 <: TopP, P2 <: TopP, M1 <: TopM, M2 <: TopM](
  first: Code2[Polar2[T1, P1], M1],
  second: Code2[Polar2[T2, P2], M2],
) extends Code2[PairS[T1, T2, P1, P2], M1 | M2]
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
