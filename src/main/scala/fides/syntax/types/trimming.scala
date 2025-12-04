package fides.syntax.types

import fides.syntax.values.Pair
import util.TopN

type TrimmedR[S <: TopS, C <: Code[S, TopQ], RC <: Code[S, SomeQ[TopP, BotQ]]] = TrimmedGR[S, TopN.`1`, C, RC]

sealed trait TrimmedGR[S <: TopS, Height <: TopN, C <: Code[S, TopQ], RC <: Code[S, SomeQ[TopP, BotQ]]]
// todo not sure if we should keep the type parameter S
object TrimmedGR:
  given [
    T1 <: TopT, T2 <: TopT, P <: TopP,
    S1 <: Polar2[T1, P], S2 <: Polar2[T2, P], H <: TopP, Q <: TopQ,
    Height <: TopN, C1 <: Code[S1, SomeQ[H, Q]], C2 <: Code[S2, SomeQ[H, Q]],
    RC1 <: Code[S1, SomeQ[H, BotQ]], RC2 <: Code[S2, SomeQ[H, BotQ]],
  ] => (TrimmedGR[S1, Height, C1, RC1], TrimmedGR[S2, Height, C2, RC2]) => TrimmedGR[
    PairS[T1, T2, P, S1, S2], Height,
    Pair[T1, T2, P, S1, S2, SomeQ[H, Q], C1, C2],
    Pair[T1, T2, P, S1, S2, SomeQ[H, BotQ], RC1, RC2],
  ]
end TrimmedGR
