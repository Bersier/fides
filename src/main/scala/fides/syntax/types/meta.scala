package fides.syntax.types

type TopQ = Q[TopS]

sealed trait Q[+S <: TopS]

final abstract class EscapeQ[S <: TopS, +ES <: Polar2[QuotedT[S], ?], Level] extends Q[S]

final abstract class PairQ[
  T1 <: TopT,
  T2 <: TopT,
  +P <: TopP,
  +S1 <: Polar2[T1, P],
  +S2 <: Polar2[T2, P],
  +Q1 <: Q[S1],
  +Q2 <: Q[S2],
] extends Q[PairS[T1, T2, P, S1, S2]]

// todo variance
// todo add M here instead?
