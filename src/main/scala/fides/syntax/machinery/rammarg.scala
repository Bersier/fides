package fides.syntax.machinery

/**
  * Reverse grammar. Mirrors [[TopG]], but with reversed bounds, so that code extractors can be typed properly.
  *
  * It is not fully reversed (subtyping relations are still in the same direction),
  * but the additional reversion is obtained by inverting bounds
  * (so using it as a lower bound when an upper bound is needed conceptually, and vice versa).
  */
sealed trait TopR private[machinery]()

sealed trait PolarR[+`D+` >: BotD <: OffTopD, -`D-` >: OffBotD <: TopD, +P <: TopP]

// todo unfinished
final abstract class PairR[
  +`D1+` >: BotD <: OffTopD, -`D1-` >: OffBotD <: TopD,
  +`D2+` >: BotD <: OffTopD, -`D2-` >: OffBotD <: TopD,
  +P1 >: BotP <: TopP, +P2 >: BotP <: TopP,
  +G1 >: Polar2G[`D1+`, `D1-`, P1], +G2 >: Polar2G[`D2+`, `D2-`, P2],
] extends PolarR[PairD[`D1+`, `D2+`], PairD[`D1-`, `D2-`], P1 | P2]
