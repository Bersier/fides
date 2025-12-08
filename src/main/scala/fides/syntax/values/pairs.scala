package fides.syntax.values

import fides.syntax.machinery.*

/**
  * General polar for pairing.
  */
final case class Pair[
  D1 <: TopD, D2 <: TopD, P1 <: TopP, P2 <: TopP,
  G1 <: PolarG[D1, P1], G2 <: PolarG[D2, P2], Q1 <: TopQ, Q2 <: TopQ,
  M1 <: ConsM[G1, Q1], M2 <: ConsM[G2, Q2],
](c1: Code[M1], c2: Code[M2]) extends Code[PairM[D1, D2, P1, P2, G1, G2, Q1, Q2, M1, M2]]
// todo replace by record?

/**
  * As an Expr, the wires are sinks that collect values from the executing body, which are then output as a bundle.
  *
  * As an Xctr, the inputted bundle is unbundled along the wires,
  * which are sources that feed values to the executing body.
  */
//final case class Bundle[Q <: TopQ](wires: Any, body: ConsM[AplrG, Q]) extends ConsM[PovrG[?, ?], Q]
// todo
// todo double-bundle extends Code[Bipo...
