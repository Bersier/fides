package fides.syntax.values

import fides.syntax.machinery.*

/**
  * General polar for pairing.
  */
final case class Pair[
  `D1+` <: TopD, D1 <: `D1+`, `D1-` <: D1, `D2+` <: TopD, D2 <: `D2+`, `D2-` <: D2, P1 <: TopP, P2 <: TopP,
  G1 <: Polar2G[`D1+`, D1, `D1-`, P1], G2 <: Polar2G[`D2+`, D2, `D2-`, P2],
  M1 <: ConsHM[G1], M2 <: ConsHM[G2],
](c1: Code[M1], c2: Code[M2]) extends Code[PairM[`D1+`, D1, `D1-`, `D2+`, D2, `D2-`, P1, P2, G1, G2, M1, M2]]
// todo replace by record?

/**
  * As an Expr, the wires are sinks that collect values from the executing body, which are then output as a bundle.
  *
  * As an Xctr, the inputted bundle is unbundled along the wires,
  * which are sources that feed values to the executing body.
  */
//final case class Bundle(wires: Any, body: ConsM[AplrG]) extends ConsM[PovrG[?, ?]]
// todo
// todo double-bundle extends Code[Bipo...
