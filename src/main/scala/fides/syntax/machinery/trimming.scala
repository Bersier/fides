package fides.syntax.machinery

type TrimmedR[G <: TopG, M <: ConsM[G, TopQ], RM <: ConsM[G, ConsQ[TopP, BotQ]]] = TrimmedGR[G, TopN.`1`, M, RM]

sealed trait TrimmedGR[G <: TopG, Height <: TopN, M <: ConsM[G, TopQ], RM <: ConsM[G, ConsQ[TopP, BotQ]]]
// todo not sure if we should keep the type parameter G
object TrimmedGR:
  given [
    D1 <: TopD, D2 <: TopD, P1 <: TopP, P2 <: TopP,
    G1 <: PolarG[D1, P1], G2 <: PolarG[D2, P2], H1 <: TopP, H2 <: TopP, Q1 <: TopQ, Q2 <: TopQ,
    Height <: TopN, M1 <: ConsM[G1, ConsQ[H1, Q1]], M2 <: ConsM[G2, ConsQ[H2, Q2]],
    RM1 <: ConsM[G1, ConsQ[H1, BotQ]], RM2 <: ConsM[G2, ConsQ[H2, BotQ]],
  ] => (TrimmedGR[G1, Height, M1, RM1], TrimmedGR[G2, Height, M2, RM2]) => TrimmedGR[
    PairG[D1, D2, P1, P2, G1, G2], Height,
    PairM[D1, D2, P1, P2, G1, G2, ConsQ[H1, Q1],  ConsQ[H2, Q2], M1, M2],
    PairM[D1, D2, P1, P2, G1, G2, ConsQ[H1, BotQ], ConsQ[H2, BotQ], RM1, RM2],
  ]
end TrimmedGR
