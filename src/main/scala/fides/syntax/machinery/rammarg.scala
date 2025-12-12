package fides.syntax.machinery

sealed trait OffTopR private[machinery]()

transparent sealed trait PairOffTopR[
  +`D1+` >: BotD <: OffTopD, -`D1-` >: OffBotD <: TopD,
  +`D2+` >: BotD <: OffTopD, -`D2-` >: OffBotD <: TopD,
  +P1 >: BotP <: TopP, +P2 >: BotP <: TopP,
  +G1 <: OffTopR, +G2 <: OffTopR,
] extends OffTopR

/**
  * Reverse grammar. Mirrors [[TopG]], but with reversed bounds, so that code extractors can be typed properly.
  *
  * It is not fully reversed (subtyping relations are still in the same direction),
  * but the additional reversion is obtained by inverting bounds
  * (so using it as a lower bound when an upper bound is needed conceptually, and vice versa).
  */
sealed trait TopR extends PairOffTopR[OffTopD, OffBotD, OffTopD, OffBotD, TopP, TopP, TopR, TopR]

sealed trait PolarR[+`D+` >: BotD <: OffTopD, -`D-` >: OffBotD <: TopD, +P <: TopP] extends TopR

sealed trait PairR[
  +`D1+` >: BotD <: OffTopD, -`D1-` >: OffBotD <: TopD,
  +`D2+` >: BotD <: OffTopD, -`D2-` >: OffBotD <: TopD,
  +P1 >: BotP <: TopP, +P2 >: BotP <: TopP,
  +G1 >: PolarR[`D1+`, `D1-`, P1] <: OffTopR, +G2 >: PolarR[`D2+`, `D2-`, P2] <: OffTopR,
] extends PolarR[PairD[`D1+`, `D2+`], PairD[`D1-`, `D2-`], P1 | P2], PairOffTopR[
  `D1+`, `D1-`, `D2+`, `D2-`, P1, P2, G1, G2,
]

sealed trait PolarBotR extends PairR[
  BotD, TopD, BotD, TopD, BotP, BotP, PolarR[BotD, TopD, BotD], PolarR[BotD, TopD, BotD],
]

type BotR = Nothing // todo
