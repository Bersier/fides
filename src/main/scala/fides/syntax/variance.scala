package fides.syntax

// -------------------------------------------------------------------------------------------------
// This file contains boilerplate for polymorphic variance for various type hierarchies.
// -------------------------------------------------------------------------------------------------

final abstract class `E:`[+`E+` >: `-E`[OffTopD] <: `+E`[OffTopD], -`E-` >: `-E`[OffTopD] <: `+E`[OffTopD]]
type `OffTopE:` = `E:`[`+E`[OffTopD], `-E`[OffTopD]]
type `TopE:` = `E:`[`+E`[TopD], `-E`[OffTopD]] | `E:`[`+E`[OffTopD], `-E`[TopD]]
type `E+`[+E >: `-E`[TopD] <: `+E`[TopD]] = `E:`[E, `-E`[OffTopD]]
type `E-`[-E >: `-E`[TopD] <: `+E`[TopD]] = `E:`[`+E`[OffTopD], E]
type `E0`[E >: `-E`[TopD] <: `+E`[TopD]] = `E:`[E, E]
type `BotE:` = `E:`[`-E`[TopD], `+E`[TopD]]
type `OffBotE:` = `E:`[`-E`[OffTopD], `+E`[OffTopD]]

/**
  * Fixes [[EX]] from [[E]].
  *
  * @tparam EX `E:[+E, -E]`
  */
sealed trait EXR[E >: `-E`[TopD] <: `+E`[TopD], EX >: `BotE:` <: `TopE:`]
object EXR:
  given [
    `+ E` >: `-E`[TopD] <: `+E`[TopD],
    `- E` >: `-E`[TopD] <: `+E`[TopD],
  ] => EInvR[`+ E`, `- E`] => EXR[`+ E`, `E:`[`+ E`, `- E`]]
end EXR

final abstract class `E::`[+`E:+` >:`BotE:` <: `OffTopE:`, -`E:-` >: `OffBotE:` <:`TopE:`]
type `TopE::` = `E::`[`TopE:`, `OffBotE:`] | `E::`[`OffTopE:`, `BotE:`]
type `E:+`[+`E:` >:`BotE:` <:`TopE:`] = `E::`[`E:`, `BotE:`]
type `E:-`[-`E:` >:`BotE:` <:`TopE:`] = `E::`[`TopE:`, `E:`]
type `E:0`[`E:` >:`BotE:` <:`TopE:`] = `E::`[`E:`, `E:`]
type `BotE::` = `E::`[`BotE:`,`TopE:`]

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

type `TopRecordE:` = `E:`[`+E`[RecordD], `-E`[OffTopD]] | `E:`[`+E`[OffTopD], `-E`[RecordD]]
