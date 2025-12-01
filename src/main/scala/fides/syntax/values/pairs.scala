package fides.syntax.values

import fides.syntax.types.*

/**
  * General [[Polar]] for pairing.
  */
final case class Pair[T1 <: TopT, T2 <: TopT, P <: TopP, S1 <: Polar2[T1, P], S2 <: Polar2[T2, P], M <: TopM](
  first: Code2[S1, M],
  second: Code2[S2, M],
) extends Code2[PairS[T1, T2, P, S1, S2], M]
// todo replace by record?

/**
  * As an Expr, the wires are sinks that collect values from the executing body, which are then output as a bundle.
  *
  * As an Xctr, the inputted bundle is unbundled along the wires,
  * which are sources that feed values to the executing body.
  */
final case class Bundle[M <: TopM](wires: Any, body: Code2[Aplr, M]) extends Code2[Povr[?, ?], M]
// todo
// todo double-bundle extends Code[Bipo...
