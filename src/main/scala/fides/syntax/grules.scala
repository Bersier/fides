package fides.syntax

// -------------------------------------------------------------------------------------------------
// This file contains additional grammar rules that are not directly expressible in terms of type
// parameter bounds.
//
// The corresponding Gs have to satisfy these rules to be valid.
// 
// Some rules defined here can be used to fix the loose `SelfD` type parameters present in
// `grammar.scala`.
// -------------------------------------------------------------------------------------------------

//region ==== `SelfD` Rules ====

sealed trait NonEmptyRecordGR[
  Key >: BotK <: TopK, -Value <: PolarG[`TopD:`], -Tail <: RecordG,
  SelfD >: `BotD:` <: `TopD:`,
]
object NonEmptyRecordGR:
  given [
    ValueType >: `BotD:` <: `TopD:`,
    Key >: BotK <: TopK, Value <: PolarG[ValueType],
    SelfD >: `BotD:` <: `TopD:`,
  ] => NonEmptyRecordDR[Key, ValueType, `D0`[EmptyRecordD], SelfD] => NonEmptyRecordGR[Key, Value, EmptyRecordG, SelfD]
  given [
    ValueType >: `BotD:` <: `TopD:`, TailType >: `BotD:` <: `D:`[RecordD, OffBotD] | `D:`[OffTopD, `-RecordD`],
    Key >: BotK <: TopK, Value <: PolarG[ValueType],
    Tail <: NonEmptyRecordG[
      TailType, TopK, PolarG[`TopD:`], RecordG,
    ],
    SelfD >: `BotD:` <: `TopD:`,
  ] => NonEmptyRecordDR[Key, ValueType, TailType, SelfD] => NonEmptyRecordGR[Key, Value, Tail, SelfD]
end NonEmptyRecordGR

sealed trait VariantGR[
  Key >: BotK <: TopK, -Value <: PolarG[`TopD:`],
  SelfD >: `BotD:` <: `TopD:`,
]
object VariantGR:
  given [
    ValueType >: `BotD:` <: `TopD:`,
    Key >: BotK <: TopK, Value <: PolarG[ValueType],
    SelfD >: `BotD:` <: `TopD:`,
  ] => VariantDR[Key, ValueType, SelfD] => VariantGR[Key, Value, SelfD]
end VariantGR

//endregion - `SelfD` Rules

//region ==== Other Rules ====

sealed trait ConcurrentGR[Processes <: ArgsG]
object ConcurrentGR:
  given ConcurrentGR[EmptyArgsG]
  given [
    Head <: ApolarG, Tail <: ArgsG,
  ] => ConcurrentGR[Tail] => ConcurrentGR[NonEmptyArgsG[Head, Tail]]
end ConcurrentGR

sealed trait SendGR[Contents <: ExprG[TopD], Recipient <: ExprG[AddressD[TopK, BotD]]]
object SendGR:
  given [
    Datatype >: BotD <: TopD,
    Contents <: ExprG[Datatype], Recipient <: ExprG[AddressD[TopK, Datatype]],
  ] => SendGR[Contents, Recipient]
end SendGR

//endregion - Other Rules
