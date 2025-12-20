package fides.syntax

sealed trait NonEmptyRecordGR[
  -Key >: BotK <: TopK, -Value >: `-PolarG` <: PolarG[`TopD:`], -Tail >: `-RecordG` <: RecordG[`TopD:`],
  SelfD >: `BotD:` <: `TopD:`,
]
object NonEmptyRecordGR:
  given [
    ValueType >: `BotD:` <: `TopD:`, TailType >: `BotD:` <: `D:`[RecordD, OffBotD] | `D:`[OffTopD, `-RecordD`],
    Key >: BotK <: TopK, Value >: `-PolarG` <: PolarG[ValueType], Tail >: `-RecordG` <: RecordG[TailType],
    SelfD >: `BotD:` <: `TopD:`,
  ] => NonEmptyRecordDR[Key, ValueType, TailType, SelfD] => NonEmptyRecordGR[Key, Value, Tail, SelfD]
end NonEmptyRecordGR

sealed trait VariantGR[
  -Key >: BotK <: TopK, -Value >: `-PolarG` <: PolarG[`TopD:`],
  SelfD >: `BotD:` <: `TopD:`,
]
object VariantGR:
  given [
    ValueType >: `BotD:` <: `TopD:`,
    Key >: BotK <: TopK, Value >: `-PolarG` <: PolarG[ValueType],
    SelfD >: `BotD:` <: `TopD:`,
  ] => VariantDR[Key, ValueType, SelfD] => VariantGR[Key, Value, SelfD]
end VariantGR

sealed trait IdentifierGR[
  -K >: `BotK:` <: `TopK:`,
  SelfD >: `BotD:` <: `TopD:`,
]
object IdentifierGR:
  given [
    K >: `BotK:` <: `TopK:`,
    SelfD >: `BotD:` <: `TopD:`,
  ] => IdentifierDR[K, SelfD] => IdentifierGR[K, SelfD]
end IdentifierGR
