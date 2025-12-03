package fides.syntax.values

import fides.syntax.types.*

/**
  * General [[Polar]] for pairing.
  *
  * Only [[C1]], [[C2]] and [[M]] are non-auxiliary.
  */
final case class Pair2[
  T1 <: TopT, T2 <: TopT,
  P <: TopP,
  S1 <: Polar2[T1, P], S2 <: Polar2[T2, P],
  C1 <: Scape[S1], C2 <: Scape[S2],
  M <: TopM,
](
  first: Code3[C1, M],
  second: Code3[C2, M],
) extends Code3[PairC[T1, T2, P, S1, S2, C1, C2], M]
// todo replace by record?

/**
  * General [[Polar]] for pairing.
  */
final case class Pair[T1 <: TopT, T2 <: TopT, P <: TopP, S1 <: Polar2[T1, P], S2 <: Polar2[T2, P], M <: TopM](
  first: Code2[S1, M],
  second: Code2[S2, M],
) extends Code2[PairS[T1, T2, P, S1, S2], M]
// todo replace by record?

given [
  T1 <: TopT, T2 <: TopT, P <: TopP, S1 <: Polar2[T1, P], S2 <: Polar2[T2, P], M <: TopM
] => (f: Code2[S1, M], s: Code2[S2, M]) => Code2[PairS[T1, T2, P, S1, S2], M] = Pair(f, s)

/**
  * As an Expr, the wires are sinks that collect values from the executing body, which are then output as a bundle.
  *
  * As an Xctr, the inputted bundle is unbundled along the wires,
  * which are sources that feed values to the executing body.
  */
final case class Bundle[M <: TopM](wires: Any, body: Code2[Aplr, M]) extends Code2[Povr[?, ?], M]
// todo
// todo double-bundle extends Code[Bipo...
