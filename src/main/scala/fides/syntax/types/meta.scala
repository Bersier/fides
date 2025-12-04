package fides.syntax.types

type TopC = Scape[TopS, TopM]

sealed trait Scape[+S <: TopS, +M <: TopM]
// todo could we keep a stack of Ss, instead of a single one?

final abstract class QuoteC[
  S <: TopS, P <: TopP, M <: TopM,
  +C <: Scape[S, SomeM[P, M]], +RC <: Scape[S, SomeM[P, BotM]],
](using TrimmedR[S, C, RC]) extends Scape[QuoteS[S, P, RC], M]
// todo using doesn't do anything for a phantom type. Looks like we might have to get rid of the Code wrapper

final abstract class EscapeC[
  S <: TopS, P <: TopP, M <: TopM,
  +C <: Scape[Polar2[QuoteT[S], P], M],
] extends Scape[S, SomeM[P, M]]
// todo Level

final abstract class PairC[
  T1 <: TopT, T2 <: TopT, P <: TopP,
  S1 <: Polar2[T1, P], S2 <: Polar2[T2, P], M <: TopM,
  +C1 <: Scape[S1, M], +C2 <: Scape[S2, M],
] extends Scape[PairS[T1, T2, P, S1, S2], M]
