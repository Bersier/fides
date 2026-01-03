package fides.syntax

// -------------------------------------------------------------------------------------------------
// This file contains boilerplate for polymorphic variance for various type hierarchies.
// -------------------------------------------------------------------------------------------------

final abstract class `D:`[+`D+` >: ND[OffTopD] <: PD[OffTopD], -`D-` >: ND[OffTopD] <: PD[OffTopD]]
type `OffTopD:` = `D:`[PD[OffTopD], ND[OffTopD]]
type `TopD:` = `D:`[PD[TopD], ND[OffTopD]] | `D:`[PD[OffTopD], ND[TopD]]
type `D+`[+D >: ND[TopD] <: PD[TopD]] = `D:`[D, ND[OffTopD]]
type `D-`[-D >: ND[TopD] <: PD[TopD]] = `D:`[PD[OffTopD], D]
type `D0`[D >: ND[TopD] <: PD[TopD]] = `D:`[D, D]
type `BotD:` = `D:`[ND[TopD], PD[TopD]]
type `OffBotD:` = `D:`[ND[OffTopD], PD[OffTopD]]

/**
  * Fixes [[DX]] from [[D]].
  *
  * @tparam DX `D:[+D, -D]`
  */
sealed trait DXR[D >: ND[TopD] <: PD[TopD], DX >: `BotD:` <: `TopD:`]
object DXR:
  given [
    `+ D` >: ND[TopD] <: PD[TopD],
    `- D` >: ND[TopD] <: PD[TopD],
  ] => DInvR[`+ D`, `- D`] => DXR[`+ D`, `D:`[`+ D`, `- D`]]
end DXR

final abstract class `D::`[+`D:+` >:`BotD:` <: `OffTopD:`, -`D:-` >: `OffBotD:` <:`TopD:`]
type `TopD::` = `D::`[`TopD:`, `OffBotD:`] | `D::`[`OffTopD:`, `BotD:`]
type `D:+`[+`D:` >:`BotD:` <:`TopD:`] = `D::`[`D:`, `BotD:`]
type `D:-`[-`D:` >:`BotD:` <:`TopD:`] = `D::`[`TopD:`, `D:`]
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

type `TopRecordD:` = `D:`[PD[RecordD], ND[OffTopD]] | `D:`[PD[OffTopD], ND[RecordD]]
