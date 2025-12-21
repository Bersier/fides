package fides.syntax

// -------------------------------------------------------------------------------------------------
// This file contains inductive rules for building more complex `TopD:` types from
// simpler ones.
// -------------------------------------------------------------------------------------------------

sealed trait NonEmptyRecordDR[
  -Key >: BotK <: TopK, -Value >: `BotD:` <: `TopD:`,
  -Tail >: `BotD:` <: `D:`[RecordD, OffBotD] | `D:`[OffTopD, `-RecordD`],
  SelfD >: `BotD:` <: `TopD:`,
]
sealed trait NonEmptyRecordLDR:
  given [
    Key >: BotK <: TopK, Value >: BotD <: TopD, Tail >: `-RecordD` <: RecordD,
  ] => NonEmptyRecordDR[Key, `D+`[Value], `D+`[Tail], `D+`[NonEmptyRecordD[Key, Value, Tail]]]
  given [
    Key >: BotK <: TopK, Value >: BotD <: TopD, Tail >: `-RecordD` <: RecordD,
  ] => NonEmptyRecordDR[Key, `D-`[Value], `D-`[Tail], `D-`[NonEmptyRecordD[Key, Value, Tail]]]
object NonEmptyRecordDR extends NonEmptyRecordLDR:
  given [
    Key >: BotK <: TopK, `Value+` >: BotD <: TopD, `Value-` >: BotD <: TopD,
    `Tail+` >: `-RecordD` <: RecordD, `Tail-` >: `-RecordD` <: RecordD,
  ] => NonEmptyRecordDR[
    Key, `D:`[`Value+`, `Value-`], `D:`[`Tail+`, `Tail-`],
    `D:`[NonEmptyRecordD[Key, `Value+`, `Tail+`], NonEmptyRecordD[Key, `Value-`, `Tail-`]],
  ]
end NonEmptyRecordDR

sealed trait VariantDR[
  Key >: BotK <: TopK, -Value >: `BotD:` <: `TopD:`,
  SelfD >: `BotD:` <: `TopD:`,
]
sealed trait VariantLDR:
  given [Key >: BotK <: TopK, Value >: BotD <: TopD] => VariantDR[Key, `D+`[Value], `D+`[VariantD[Key, Value]]]
  given [Key >: BotK <: TopK, Value >: BotD <: TopD] => VariantDR[Key, `D-`[Value], `D-`[VariantD[Key, Value]]]
object VariantDR extends VariantLDR:
  given [
    Key >: BotK <: TopK, `Value+` >: BotD <: TopD, `Value-` >: BotD <: TopD,
  ] => VariantDR[Key, `D:`[`Value+`, `Value-`], `D:`[VariantD[Key, `Value+`], VariantD[Key, `Value-`]]]
end VariantDR
