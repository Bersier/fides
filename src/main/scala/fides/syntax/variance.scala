package fides.syntax

// -------------------------------------------------------------------------------------------------
// This file contains boilerplate for polymorphic variance for various type hierarchies.
// -------------------------------------------------------------------------------------------------

sealed trait `OffTopD:` private[syntax]()
sealed trait `D:`[+`D+` >: BotD <: OffTopD, -`D-` >: OffBotD <: TopD] extends `OffTopD:`
type `TopD:` = `D:`[OffTopD, OffBotD]
type `D+`[+D >: BotD <: TopD] = `D:`[D, OffBotD]
type `D-`[-D >: BotD <: TopD] = `D:`[OffTopD, D]
type `D0`[D >: BotD <: TopD] = `D:`[D, D]
type `BotD:` = `D:`[BotD, TopD]
final abstract class `OffBotD:` extends `BotD:`

final abstract class `D::`[+`D:+` >:`BotD:` <: `OffTopD:`, -`D:-` >: `OffBotD:` <:`TopD:`]
type `TopD::` = `D::`[`OffTopD:`, `OffBotD:`]
type `D:+`[+`D:` >:`BotD:` <:`TopD:`] = `D::`[`D:`, `OffBotD:`]
type `D:-`[-`D:` >:`BotD:` <:`TopD:`] = `D::`[`OffTopD:`, `D:`]
type `D:0`[`D:` >:`BotD:` <:`TopD:`] = `D::`[`D:`, `D:`]
type `BotD::` = `D::`[`BotD:`,`TopD:`]

final abstract class `K:`[+`K+` >: BotK <: TopK, -`K-` >: BotK <: TopK]
type `TopK:` = `K:`[TopK, BotK]
type `K+`[+K >: BotK <: TopK] = `K:`[K, BotK]
type `K-`[-K >: BotK <: TopK] = `K:`[TopK, K]
type `K0`[K >: BotK <: TopK] = `K:`[K, K]
type `BotK:` = `K:`[BotK, TopK]

final abstract class `N:`[+`N+` >: BotN <: TopN, -`N-` >: BotN <: TopN]
type `TopN:` = `N:`[TopN, BotN]
type `N+`[+N >: BotN <: TopN] = `N:`[N, BotN]
type `N-`[-N >: BotN <: TopN] = `N:`[TopN, N]
type `N0`[N >: BotN <: TopN] = `N:`[N, N]
type `BotN:` = `N:`[BotN, TopN]
