package fides.syntax

// -------------------------------------------------------------------------------------------------
// This file contains inductive rules for building more complex `TopD:` types from
// simpler ones. If Scala had bivariance, this would not be needed.
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
    Key >: BotK <: TopK, Value >: BotD <: TopD, Tail >: `-RecordD` <: RecordD,
  ] => NonEmptyRecordDR[Key, `D0`[Value], `D0`[Tail], `D0`[NonEmptyRecordD[Key, Value, Tail]]]
end NonEmptyRecordDR

sealed trait VariantDR[
  -Key >: BotK <: TopK, -Value >: `BotD:` <: `TopD:`,
  SelfD >: `BotD:` <: `TopD:`,
]
sealed trait VariantLDR:
  given [Key >: BotK <: TopK, Value >: BotD <: TopD] => VariantDR[Key, `D+`[Value], `D+`[VariantD[Key, Value]]]
  given [Key >: BotK <: TopK, Value >: BotD <: TopD] => VariantDR[Key, `D-`[Value], `D-`[VariantD[Key, Value]]]
object VariantDR extends VariantLDR:
  given [Key >: BotK <: TopK, Value >: BotD <: TopD] => VariantDR[Key, `D0`[Value], `D0`[VariantD[Key, Value]]]
end VariantDR

sealed trait IdentifierDR[
  -K >: `BotK:` <: `TopK:`,
  SelfD >: `BotD:` <: `TopD:`,
]
object IdentifierDR:
  given [K >: BotK <: TopK] => IdentifierDR[`K0`[K], `D0`[IdentifierD[K]]]
end IdentifierDR
