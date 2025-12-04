package fides.syntax.values

import fides.syntax.types.*

/**
  * General [[Polar]] for pairing.
  */
final case class Pair[
  T1 <: TopT, T2 <: TopT, P <: TopP,
  S1 <: Polar2[T1, P], S2 <: Polar2[T2, P], Q <: TopQ,
  +C1 <: Code[S1, Q], +C2 <: Code[S2, Q],
// todo now that we have removed the wrapper, we have to make this covariant in C1 and C2, which is not ideal,
//  because it could lead to lost type information; we are relying on the compiler typing stuff tightly
//  but if code is in a contravariant position, won't the compiler loosen it as needed?
//  Also, it means that illegal code can be built simply with the help of Scala type ascriptions.
//  It does come in handy for Escape.Matcher, though...
](first: C1, second: C2) extends Code[PairS[T1, T2, P, S1, S2], Q]
// todo replace by record?

given [
  T1 <: TopT, T2 <: TopT, P <: TopP,
  S1 <: Polar2[T1, P], S2 <: Polar2[T2, P], Q <: TopQ,
  C1 <: Code[S1, Q], C2 <: Code[S2, Q],
] => (f: Code[S1, Q], s: Code[S2, Q]) => Code[PairS[T1, T2, P, S1, S2], Q] = Pair(f, s)

/**
  * As an Expr, the wires are sinks that collect values from the executing body, which are then output as a bundle.
  *
  * As an Xctr, the inputted bundle is unbundled along the wires,
  * which are sources that feed values to the executing body.
  */
final case class Bundle[Q <: TopQ](wires: Any, body: Code[Aplr, Q]) extends Code[Povr[?, ?], Q]
// todo
// todo double-bundle extends Code[Bipo...
