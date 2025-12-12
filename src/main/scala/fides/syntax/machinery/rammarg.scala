package fides.syntax.machinery

type OffTopR = GenOffTopR[
  OffTopD, OffBotD, OffTopD, OffBotD, TopP, TopP, ?, ?,
]

sealed trait GenOffTopR[
  +`D1+` >: BotD <: OffTopD, -`D1-` >: OffBotD <: TopD,
  +`D2+` >: BotD <: OffTopD, -`D2-` >: OffBotD <: TopD,
  +P1 >: BotP <: TopP, +P2 >: BotP <: TopP,
  +G1 <: OffTopR, +G2 <: OffTopR,
] private[machinery]()

/**
  * Reverse grammar. Mirrors [[TopG]], but with reversed bounds, so that code extractors can be typed properly.
  *
  * It is not fully reversed (subtyping relations are still in the same direction),
  * but the additional reversion is obtained by inverting bounds
  * (so using it as a lower bound when an upper bound is needed conceptually, and vice versa).
  */
sealed trait TopR extends OffTopR

sealed trait PolarR[+`D+` >: BotD <: OffTopD, -`D-` >: OffBotD <: TopD, +P <: TopP] extends TopR

sealed trait PairR[
  +`D1+` >: BotD <: OffTopD, -`D1-` >: OffBotD <: TopD,
  +`D2+` >: BotD <: OffTopD, -`D2-` >: OffBotD <: TopD,
  +P1 >: BotP <: TopP, +P2 >: BotP <: TopP,
  +G1 >: PolarR[`D1+`, `D1-`, P1] <: OffTopR, +G2 >: PolarR[`D2+`, `D2-`, P2] <: OffTopR,
  // todo lower bounds are wrong; should be like in PairM for G1- and G2-
  //  But then, if all the grammar rules are already specified in PairM, why do we need them here (and in G) again?
  //  Now PairM also takes care never to get out of the bounds of the grammar.
  //  So I think we can use a pregrammar after all
] extends PolarR[PairD[`D1+`, `D2+`], PairD[`D1-`, `D2-`], P1 | P2], GenOffTopR[
  `D1+`, `D1-`, `D2+`, `D2-`, P1, P2, G1, G2,
]

// todo should be above OffBotR, and not below BotG; a piece of code of this type doesn't fit anywhere,
//  so it behaves just like another BotG
sealed trait PolarBotR extends PairR[
  BotD, TopD, BotD, TopD, BotP, BotP, PolarR[BotD, TopD, BotD], PolarR[BotD, TopD, BotD],
]

type BotR = Nothing // todo
