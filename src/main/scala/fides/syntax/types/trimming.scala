package fides.syntax.types

import fides.syntax.values.Pair
import util.TopN

type TrimmedR[S <: TopS, C <: Code[S, TopM], RC <: Code[S, SomeM[TopP, BotM]]] = TrimmedGR[S, TopN.`1`, C, RC]

sealed trait TrimmedGR[S <: TopS, Height <: TopN, C <: Code[S, TopM], RC <: Code[S, SomeM[TopP, BotM]]]
// todo not sure if we should keep the type parameter S
object TrimmedGR:
  given [
    T1 <: TopT, T2 <: TopT, P <: TopP,
    S1 <: Polar2[T1, P], S2 <: Polar2[T2, P], H <: TopP, M <: TopM,
    Height <: TopN, C1 <: Code[S1, SomeM[H, M]], C2 <: Code[S2, SomeM[H, M]],
    RC1 <: Code[S1, SomeM[H, BotM]], RC2 <: Code[S2, SomeM[H, BotM]],
  ] => (TrimmedGR[S1, Height, C1, RC1], TrimmedGR[S2, Height, C2, RC2]) => TrimmedGR[
    PairS[T1, T2, P, S1, S2], Height,
    Pair[T1, T2, P, S1, S2, SomeM[H, M], C1, C2],
    Pair[T1, T2, P, S1, S2, SomeM[H, BotM], RC1, RC2],
  ]
end TrimmedGR
