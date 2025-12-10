package fides.syntax.values

import fides.syntax.machinery.*

/**
  * General polar for pairing.
  */
//final case class Pair[
//  `D1++` >: BotD <: OffTopD, `D1-+` >: OffBotD <: `D1+-` & TopD,
//  `D2++` >: BotD <: OffTopD, `D2-+` >: OffBotD <: `D2+-` & TopD,
//  `P1+` >: BotP <: TopP, `P2+` >: BotP <: TopP,
//  `D1+-` >: BotD <: `D1++`, `D1--` >: `D1-+` <: `D1+-` & TopD,
//  `D2+-` >: BotD <: `D2++`, `D2--` >: `D2-+` <: `D2+-` & TopD,
//  `P1-` >: BotP <: `P1+`, `P2-` >: BotP <: `P2+`,
//  `G1+` >: Polar2G[`D1++`, `D1-+`, `P1+`], `G2+` >: Polar2G[`D2++`, `D2-+`, `P2+`],
//  `G1-` <: Polar2G[`D1+-`, `D1--`, `P1-`], `G2-` <: Polar2G[`D2+-`, `D2--`, `P2-`],
//  M1 <: GenM2[`G1+`, `G1-`], M2 <: GenM2[`G2+`, `G2-`],
//](c1: Code2[M1], c2: Code2[M2]) extends Code2[PairM[
//  `D1++`, `D1-+`, `D2++`, `D2-+`, `P1+`, `P2+`, `D1+-`, `D1--`, `D2+-`, `D2--`, `P1-`, `P2-`,
//  `G1+`, `G2+`, `G1-`, `G2-`, M1, M2,
//]]
// todo replace by record?

/**
  * As an Expr, the wires are sinks that collect values from the executing body, which are then output as a bundle.
  *
  * As an Xctr, the inputted bundle is unbundled along the wires,
  * which are sources that feed values to the executing body.
  */
//final case class Bundle(wires: Any, body: ConsM[AplrG]) extends ConsM[PovrG[?, ?]]
// todo
// todo double-bundle extends Code[Bipo...
