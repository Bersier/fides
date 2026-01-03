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
  Key >: BotK <: TopK, -Value <: PolarG[`TopE:`], -Tail <: RecordG,
  SelfD >: `BotE:` <: `TopE:`,
]
object NonEmptyRecordGR:
  given [
    ValueType >: `BotE:` <: `TopE:`,
    Key >: BotK <: TopK, Value <: PolarG[ValueType],
    SelfD >: `BotE:` <: `TopE:`,
  ] => NonEmptyRecordDR[
    Key, ValueType, `E0`[`+E`[EmptyRecordD]], SelfD,
  ] => NonEmptyRecordGR[Key, Value, EmptyRecordG, SelfD]
  given [
    ValueType >: `BotE:` <: `TopE:`, TailType >: `BotE:` <: `TopRecordE:`,
    Key >: BotK <: TopK, Value <: PolarG[ValueType],
    Tail <: NonEmptyRecordG[
      TailType, TopK, `+H`[PolarG[`TopE:`]], `+H`[RecordG],
    ],
    SelfD >: `BotE:` <: `TopE:`,
  ] => NonEmptyRecordDR[Key, ValueType, TailType, SelfD] => NonEmptyRecordGR[Key, Value, Tail, SelfD]
end NonEmptyRecordGR

sealed trait EntryGR[
  Key >: BotK <: TopK, -Value <: PolarG[`TopE:`],
  SelfD >: `BotE:` <: `TopE:`,
]
object EntryGR:
  given [
    ValueType >: `BotE:` <: `TopE:`,
    Key >: BotK <: TopK, Value <: PolarG[ValueType],
    SelfD >: `BotE:` <: `TopE:`,
  ] => EntryDR[Key, ValueType, SelfD] => EntryGR[Key, Value, SelfD]
end EntryGR

sealed trait BundleGR[
  Keys >: `-H`[ArgsG] <: `+H`[ArgsG],
  SelfD >: `BotE:` <: `TopE:`,
]
object BundleGR:
  given BundleGR[`+H`[EmptyArgsG], `E0`[`+E`[EmptyRecordD]]]
  given [
    Key >: BotK <: TopK, Datatype >: `BotE:` <: `TopE:`,
    Head >: `-H`[LocRefG[Key, Datatype]] <: `+H`[LocRefG[Key, Datatype]], Tail >: `-H`[ArgsG] <: `+H`[ArgsG],
    TailD >: `BotE:` <: `TopRecordE:`,
    SelfD >: `BotE:` <: `TopE:`,
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

sealed trait SendGR[Contents <: ExprG[`+E`[TopD]], Recipient <: ExprG[`+E`[AddressD[TopK, `-E`[TopD]]]]]
object SendGR:
  given [
    Datatype >: `-E`[TopD] <: `+E`[TopD],
    Contents <: ExprG[Datatype], Recipient <: ExprG[`+E`[AddressD[TopK, Datatype]]],
  ] => SendGR[Contents, Recipient]
end SendGR

//endregion - Other Rules
