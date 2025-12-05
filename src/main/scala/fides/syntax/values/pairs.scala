package fides.syntax.values

import fides.syntax.types.*

/**
  * General [[PolarS]] for pairing.
  */
final case class Pair[
  D1 <: TopD, D2 <: TopD, P <: TopP,
  S1 <: Polar2S[D1, P], S2 <: Polar2S[D2, P], Q <: TopQ,
  M1 <: ConsM[S1, Q], M2 <: ConsM[S2, Q],
](c1: Code[M1], c2: Code[M2]) extends Code[PairM[D1, D2, P, S1, S2, Q, M1, M2]]
// todo replace by record?

given [
  D1 <: TopD, D2 <: TopD, P <: TopP,
  S1 <: Polar2S[D1, P], S2 <: Polar2S[D2, P], Q <: TopQ,
  M1 <: ConsM[S1, Q], M2 <: ConsM[S2, Q],
] => (c1: Code[M1], c2: Code[M2]) => Code[PairM[D1, D2, P, S1, S2, Q, M1, M2]] = Pair(c1, c2)

/**
  * As an Expr, the wires are sinks that collect values from the executing body, which are then output as a bundle.
  *
  * As an Xctr, the inputted bundle is unbundled along the wires,
  * which are sources that feed values to the executing body.
  */
//final case class Bundle[Q <: TopQ](wires: Any, body: ConsM[AplrS, Q]) extends ConsM[PovrS[?, ?], Q]
// todo
// todo double-bundle extends Code[Bipo...
