package fides.syntax.types

type TopC = Scape[TopS, TopM]

sealed trait Scape[+S <: TopS, +M <: TopM]

final abstract class QuoteC[
  S <: TopS, P <: TopP, M <: TopM,
  C <: Scape[S, SomeM[P, M]], RC <: Scape[S, SomeM[P, BotM]],
](using TrimmedR[S, C, RC]) extends Scape[QuoteS[S, P, RC], M]

sealed trait TrimmedR[S <: TopS, C <: Scape[S, TopM], RC <: Scape[S, SomeM[TopP, BotM]]]
// todo not sure if we should keep the type parameter S
object TrimmedR:
  given [
    T1 <: TopT, T2 <: TopT, P <: TopP,
    S1 <: Polar2[T1, P], S2 <: Polar2[T2, P], H <: TopP, M <: TopM,
    C1 <: Scape[S1, SomeM[H, M]], C2 <: Scape[S2, SomeM[H, M]],
    RC1 <: Scape[S1, SomeM[H, BotM]], RC2 <: Scape[S2, SomeM[H, BotM]],
  ] => (TrimmedR[S1, C1, RC1], TrimmedR[S2, C2, RC2]) => TrimmedR[
    PairS[T1, T2, P, S1, S2],
    PairC[T1, T2, P, S1, S2, SomeM[H, M], C1, C2],
    PairC[T1, T2, P, S1, S2, SomeM[H, BotM], RC1, RC2],
  ]()
end TrimmedR

final abstract class EscapeC[
  S <: TopS, P <: TopP,
  ES <: Polar2[QuotedT[S], P], M <: TopM, // todo note that ES cannot just be any TopS...
  +C <: Scape[ES, M],
] extends Scape[S, SomeM[P, M]]
// todo Level

final abstract class PairC[
  T1 <: TopT, T2 <: TopT, P <: TopP,
  S1 <: Polar2[T1, P], S2 <: Polar2[T2, P], M <: TopM,
  +C1 <: Scape[S1, M], +C2 <: Scape[S2, M],
] extends Scape[PairS[T1, T2, P, S1, S2], M]
