package fides.syntax.types

import fides.syntax.values.Pair

sealed trait TrimmedR[S <: TopS, C <: Code[S, TopM], RC <: Code[S, SomeM[TopP, BotM]]]
// todo not sure if we should keep the type parameter S
object TrimmedR:
  given [
    T1 <: TopT, T2 <: TopT, P <: TopP,
    S1 <: Polar2[T1, P], S2 <: Polar2[T2, P], H <: TopP, M <: TopM,
    C1 <: Code[S1, SomeM[H, M]], C2 <: Code[S2, SomeM[H, M]],
    RC1 <: Code[S1, SomeM[H, BotM]], RC2 <: Code[S2, SomeM[H, BotM]],
  ] => (TrimmedR[S1, C1, RC1], TrimmedR[S2, C2, RC2]) => TrimmedR[
    PairS[T1, T2, P, S1, S2],
    Pair[T1, T2, P, S1, S2, SomeM[H, M], C1, C2],
    Pair[T1, T2, P, S1, S2, SomeM[H, BotM], RC1, RC2],
  ]()
end TrimmedR
