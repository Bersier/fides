package fides.syntax.machinery

type TrimmedR[
  G <: TopG, P <: TopP, Q <: TopQ,
  M <: ConsM[G, ConsQ[P, Q]], TM <: ConsM[G, ConsQ[P, BotQ]],
] = TrimmedMR[TopN.`1`, M, TM]

/**
  * `G <: `[[TopG]]`, Q <: `[[TopQ]]`, TQ <: Q, H <: `[[TopN]]`, M <: `[[ConsM]]`[G, Q], TM <: `[[ConsM]]`[G, TQ]`
  */
sealed trait TrimmedMR[-H <: TopN, M <: TopM, TM <: TopM]
object TrimmedMR:

  /**
    * Note that in this case, [[TrimmedR]]`[G, P, Q, M, ITM]` and [[TrimmedR]]`[G, HTM, TM]` should also hold.
    *
    * `HTQ <: Q`
    */
  given [
    G <: TopG, P <: TopP, Q <: TopQ, TQ <: TopQ,
    ITM <: ConsM[G, ConsQ[P, BotQ]],
    H <: TopN,
    M <: ConsM[G, ConsQ[P, Q]],
    TM <: ConsM[G, ConsQ[P, TQ]],
  ] => (
    TrimmedMR[TopN.S[H], M, TM],
  ) => TrimmedMR[
    H,
    QuoteM[G, P, ITM, Q, M],
    QuoteM[G, P, ITM, TQ, TM],
  ]

  given [
    G <: TopG,
    M <: EscapeM[G, TopQ],
  ] => TrimmedMR[TopN.Z, M, ConsM[G, BotQ]]

  /**
    * `TQ <: Q`
    */
  given [
    G <: TopG, Q <: TopQ, TQ <: TopQ,
    H <: TopN,
  ] => TrimmedQR[H, Q, TQ] => TrimmedMR[
    H,
    EscapeM[G, Q],
    EscapeM[G, TQ],
  ]

  /**
    * `TQ <: Q`
    */
  given [
    G <: TopG, Q <: TopQ, TQ <: TopQ,
    H <: TopN,
    M <: EscapeM[G, Q],
    TM <: EscapeM[G, TQ],
  ] => (TrimmedQR[H, Q, TQ], TrimmedMR[H, M, TM]) => TrimmedMR[
    TopN.S[H],
    EscapeM.Step[G, Q, M],
    EscapeM.Step[G, TQ, TM],
  ]

  /**
    * `TQ <: Q`
    */
  given [
    G <: TopG, P <: TopP, Q <: TopQ, TQ <: TopQ,
    H <: TopN,
    M <: ConsM[PolarG[QuoteD[G], P], Q],
    TM <: ConsM[PolarG[QuoteD[G], P], TQ],
  ] => (TrimmedQR[H, Q, TQ], TrimmedMR[H, M, TM]) => TrimmedMR[
    TopN.S[H],
    EscapeM.Head[G, P, Q, M],
    EscapeM.Head[G, P, TQ, TM],
  ]

  /**
    * `TQ1 <: Q1, TQ2 <: Q2`
    */
  given [
    D1 <: TopD, D2 <: TopD, P1 <: TopP, P2 <: TopP,
    G1 <: PolarG[D1, P1], G2 <: PolarG[D2, P2],
    H1 <: TopP, H2 <: TopP, Q1 <: TopQ, Q2 <: TopQ, TQ1 <: TopQ, TQ2 <: TopQ,
    H <: TopN,
    M1 <: ConsM[G1, ConsQ[H1, Q1]], M2 <: ConsM[G2, ConsQ[H2, Q2]],
    TM1 <: ConsM[G1, ConsQ[H1, TQ1]], TM2 <: ConsM[G2, ConsQ[H2, TQ2]],
  ] => (TrimmedMR[H, M1, TM1], TrimmedMR[H, M2, TM2]) => TrimmedMR[
    H,
    PairM[D1, D2, P1, P2, G1, G2, ConsQ[H1, Q1],  ConsQ[H2, Q2], M1, M2],
    PairM[D1, D2, P1, P2, G1, G2, ConsQ[H1, TQ1], ConsQ[H2, TQ2], TM1, TM2],
  ]

  /**
    * The rule below is admissible (and probably unhelpful for actual inference).
    * It is here only for documentation purposes.
    */
  protected given [
    H <: TopN, M <: ConsM[TopG, TopQ], TM <: ConsM[TopG, TopQ], TTM <: ConsM[TopG, TopQ],
  ] => (TrimmedMR[TopN.S[H], M, TM], TrimmedMR[H, TM, TTM]) => TrimmedMR[H, M, TTM]
end TrimmedMR

/**
  * `TQ <: Q`
  */
sealed trait TrimmedQR[Height <: TopN, Q <: TopQ, TQ <: TopQ]
object TrimmedQR:
  given [Q <: TopQ] => TrimmedQR[TopN.Z, Q, BotQ]
  given [
    H <: TopN, P <: TopP, Q <: TopQ, TQ <: Q,
  ] => TrimmedQR[H, Q, TQ] => TrimmedQR[TopN.S[H], ConsQ[P, Q], ConsQ[P, TQ]]
end TrimmedQR
