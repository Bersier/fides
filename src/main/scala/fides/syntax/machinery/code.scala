package fides.syntax.machinery

import fides.syntax.values.*

/**
  * Parent type of all actual Fides code
  * <br><br>
  * This additional type wrapper is important mainly because it keeps track of [[M]] invariantly.
  * It also provides the required flexibility for escape matchers to have an [[M]]
  * type that is a supertype of that of actual escapes.
  */
trait Code[M <: TopM] private[syntax]()

trait Code2[M <: TopM2] private[syntax]() // todo add `TM+` and `TM-`. Alternatively, add a quote name list for context
// todo perhaps reduction could even be applied just-in-time, so not in advance as soon as a quote is encountered,
//  but lazily inside as we go... does that make any sense?
object Code2:
//  given [
//  `D1++` >: BotD <: OffTopD, `D1-+` >: OffBotD <: `D1+-` & TopD,
//  `D2++` >: BotD <: OffTopD, `D2-+` >: OffBotD <: `D2+-` & TopD,
//  `P1+` >: BotP <: TopP, `P2+` >: BotP <: TopP,
//  `D1+-` >: BotD <: `D1++`, `D1--` >: `D1-+` <: `D1+-` & TopD,
//  `D2+-` >: BotD <: `D2++`, `D2--` >: `D2-+` <: `D2+-` & TopD,
//  `P1-` >: BotP <: `P1+`, `P2-` >: BotP <: `P2+`,
//  `G1+` >: Polar2G[`D1++`, `D1-+`, `P1+`], `G2+` >: Polar2G[`D2++`, `D2-+`, `P2+`],
//  `G1-` <: Polar2G[`D1+-`, `D1--`, `P1-`], `G2-` <: Polar2G[`D2+-`, `D2--`, `P2-`],
//  M1 <: GenM2[`G1+`, `G1-`], M2 <: GenM2[`G2+`, `G2-`],
//] => (c1: Code2[M1], c2: Code2[M2]) => Code2[PairM[
//  `D1++`, `D1-+`, `D2++`, `D2-+`, `P1+`, `P2+`, `D1+-`, `D1--`, `D2+-`, `D2--`, `P1-`, `P2-`,
//  `G1+`, `G2+`, `G1-`, `G2-`, M1, M2,
//]] = Pair(c1, c2)
end Code2

@deprecated
trait OldCode[+G <: TopG] private[syntax]()
