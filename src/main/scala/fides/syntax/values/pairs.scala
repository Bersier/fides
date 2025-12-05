package fides.syntax.values

import fides.syntax.types.*

/**
  * General [[PolarS]] for pairing.
  */
final case class Pair[
  D1 <: TopD, D2 <: TopD, P <: TopP,
  S1 <: Polar2S[D1, P], S2 <: Polar2S[D2, P], Q <: TopQ,
  +M1 <: ConsM[S1, Q], +M2 <: ConsM[S2, Q],
// todo now that we have removed the wrapper, we have to make this covariant in M1 and M2, which is not ideal,
//  because it could lead to lost type information; we are relying on the compiler typing stuff tightly
//  but if code is in a contravariant position, won't the compiler loosen it as needed?
//  Also, it means that illegal code can be built simply with the help of Scala type ascriptions.
//  It does come in handy for Escape.Matcher, though...
](first: M1, second: M2) extends ConsM[PairS[D1, D2, P, S1, S2], Q]
// todo replace by record?

given [
  D1 <: TopD, D2 <: TopD, P <: TopP,
  S1 <: Polar2S[D1, P], S2 <: Polar2S[D2, P], Q <: TopQ,
  M1 <: ConsM[S1, Q], M2 <: ConsM[S2, Q],
] => (f: ConsM[S1, Q], s: ConsM[S2, Q]) => ConsM[PairS[D1, D2, P, S1, S2], Q] = Pair(f, s)

/**
  * As an Expr, the wires are sinks that collect values from the executing body, which are then output as a bundle.
  *
  * As an Xctr, the inputted bundle is unbundled along the wires,
  * which are sources that feed values to the executing body.
  */
final case class Bundle[Q <: TopQ](wires: Any, body: ConsM[AplrS, Q]) extends ConsM[PovrS[?, ?], Q]
// todo
// todo double-bundle extends Code[Bipo...
