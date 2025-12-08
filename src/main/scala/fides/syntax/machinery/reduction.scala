package fides.syntax.machinery

sealed trait MReductionR[-H <: TopN, Q <: TopQ, TQ <: TopQ, M <: ConsM[TopG, Q], TM <: ConsM[TopG, TQ]]
object MReductionR:

  given [
    P <: TopP, `P'` <: TopP, ITM <: TopM, TITM <: TopM,
    H <: TopN, Q <: TopQ, TQ <: TopQ,
    M <: ConsM[TopG, ConsQ[P, Q]],
    TM <: ConsM[TopG, ConsQ[`P'`, TQ]],
  ] => MReductionR[
    TopN.S[H], ConsQ[P, Q], ConsQ[`P'`, TQ],
    M,
    TM,
  ] => MReductionR[
    H, Q, TQ,
    QuoteM[P, Q, ITM, M],
    QuoteM[`P'`, TQ, TITM, TM],
  ]

  given [
    G <: TopG,
    Q <: TopQ,
    M <: EscapeM[G, Q],
  ] => MReductionR[
    TopN.Z, Q, BotQ,
    M,
    ConsM[G, BotQ],
  ]

  given [
    G <: TopG,
    H <: TopN, Q <: TopQ, TQ <: TopQ,
  ] => TrimmedQR[H, Q, TQ] => MReductionR[
    H, Q, TQ,
    EscapeM[G, Q],
    EscapeM[G, TQ],
  ]

  given [
    G <: TopG,
    H <: TopN, Q <: TopQ, TQ <: TopQ,
    M <: EscapeM[G, Q],
    TM <: EscapeM[G, TQ],
  ] => MReductionR[H, Q, TQ, M, TM] => MReductionR[
    TopN.S[H], ConsQ[BotP, Q], ConsQ[BotP, TQ],
    EscapeM.Step[G, Q, M],
    EscapeM.Step[G, TQ, TM],
  ]

  given [
    G <: TopG, P <: TopP,
    H <: TopN, Q <: TopQ, TQ <: TopQ,
    M <: ConsM[PolarG[QuoteD[G], P], Q],
    TM <: ConsM[PolarG[QuoteD[G], P], TQ],
  ] => MReductionR[H, Q, TQ, M, TM] => MReductionR[
    TopN.S[H], ConsQ[P | GenP[BotB, BotB, TopB], Q], ConsQ[P | GenP[BotB, BotB, TopB], TQ],
    EscapeM.Head[G, P, Q, M],
    EscapeM.Head[G, P, TQ, TM],
  ]

  given [
    D1 <: TopD, D2 <: TopD, P1 <: TopP, P2 <: TopP,
    G1 <: PolarG[D1, P1], G2 <: PolarG[D2, P2],
    H <: TopN, Q1 <: TopQ, Q2 <: TopQ, TQ1 <: TopQ, TQ2 <: TopQ,
    M1 <: ConsM[G1, Q1], M2 <: ConsM[G2, Q2],
    TM1 <: ConsM[G1, TQ1], TM2 <: ConsM[G2, TQ2],
  ] => (MReductionR[H, Q1, TQ1, M1, TM1], MReductionR[H, Q2, TQ2, M2, TM2]) => MReductionR[
    H, Q1 | Q2, TQ1 | TQ2,
    PairM[D1, D2, P1, P2, G1, G2, Q1, Q2, M1, M2],
    PairM[D1, D2, P1, P2, G1, G2, TQ1, TQ2, TM1, TM2],
  ]
end MReductionR

sealed trait QReductionR[H <: TopN, Q <: TopQ, TQ <: TopQ]
object QReductionR:

  given [Q <: TopQ] => QReductionR[TopN.Z, Q, BotQ]

  given [
    H <: TopN, P <: TopP, Q <: TopQ, TQ <: TopQ,
  ] => QReductionR[H, Q, TQ] => QReductionR[TopN.S[H], ConsQ[P, Q], ConsQ[P, TQ]]
end QReductionR
