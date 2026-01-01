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

// todo replace some G parameters by H parameters?

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
      TailType, TopK, `+H`[PolarG[`TopD:`]], `+H`[RecordG],
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

sealed trait BundleGR[
  Keys >: `-H`[ArgsG] <: `+H`[ArgsG],
  SelfD >: `BotD:` <: `TopD:`,
]
object BundleGR:
  given BundleGR[`+H`[EmptyArgsG], `D0`[EmptyRecordD]]
  given [
    Key >: BotK <: TopK, Datatype >: `BotD:` <: `TopD:`,
    Head >: `-H`[LocRefG[Key, Datatype]] <: `+H`[LocRefG[Key, Datatype]], Tail >: `-H`[ArgsG] <: `+H`[ArgsG],
    TailD >: `BotD:` <: `D:`[RecordD, OffBotD] | `D:`[OffTopD, `-RecordD`],
    SelfD >: `BotD:` <: `TopD:`,
  ] => BundleGR[Tail, TailD] => NonEmptyRecordDR[
    Key, Datatype, TailD, SelfD,
  ] => BundleGR[`+H`[NonEmptyArgsG[Head, Tail]], SelfD]
end BundleGR

//endregion - `SelfD` Rules

//region ==== Other Rules ====

sealed trait ConcurrentGR[Processes >: `-H`[ArgsG] <: `+H`[ArgsG]]
object ConcurrentGR:
  given [
    ProcessesG <: ArgsG,
    Processes >: `-H`[ProcessesG] <: `+H`[ProcessesG],
  ] => BoundedArgs[ProcessesG, ApolarG] => ConcurrentGR[Processes]
end ConcurrentGR

sealed trait BoundedArgs[G <: ArgsG, Bound <: TopG]
object BoundedArgs:
  given [Bound <: TopG] => BoundedArgs[EmptyArgsG, Bound]
  given [
    TailG <: ArgsG,
    Head >: `-H`[Bound] <: `+H`[Bound], TailH >: `-H`[TailG] <: `+H`[TailG],
    Bound <: TopG,
  ] => BoundedArgs[TailG, Bound] => BoundedArgs[NonEmptyArgsG[Head, TailH], Bound]
end BoundedArgs

sealed trait SendGR[Contents <: ExprG[TopD], Recipient <: ExprG[AddressD[TopK, BotD]]]
object SendGR:
  given [
    Datatype >: BotD <: TopD,
    Contents <: ExprG[Datatype], Recipient <: ExprG[AddressD[TopK, Datatype]],
  ] => SendGR[Contents, Recipient]
end SendGR

//endregion - Other Rules
