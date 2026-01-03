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

// todo replace some G parameters by PG/NG parameters?

sealed trait NonEmptyRecordGR[
  Key >: BotK <: TopK, -Value <: PolarG[`TopD:`], -Tail <: RecordG,
  SelfD >: `BotD:` <: `TopD:`,
]
object NonEmptyRecordGR:
  given [
    ValueType >: `BotD:` <: `TopD:`,
    Key >: BotK <: TopK, Value <: PolarG[ValueType],
    SelfD >: `BotD:` <: `TopD:`,
  ] => NonEmptyRecordDR[
    Key, ValueType, `D0`[PD[EmptyRecordD]], SelfD,
  ] => NonEmptyRecordGR[Key, Value, EmptyRecordG, SelfD]
  given [
    ValueType >: `BotD:` <: `TopD:`, TailType >: `BotD:` <: `TopRecordD:`,
    Key >: BotK <: TopK, Value <: PolarG[ValueType],
    Tail <: NonEmptyRecordG[
      TailType, TopK, PG[PolarG[`TopD:`]], PG[RecordG],
    ],
    SelfD >: `BotD:` <: `TopD:`,
  ] => NonEmptyRecordDR[Key, ValueType, TailType, SelfD] => NonEmptyRecordGR[Key, Value, Tail, SelfD]
end NonEmptyRecordGR

sealed trait EntryGR[
  Key >: BotK <: TopK, -Value <: PolarG[`TopD:`],
  SelfD >: `BotD:` <: `TopD:`,
]
object EntryGR:
  given [
    ValueType >: `BotD:` <: `TopD:`,
    Key >: BotK <: TopK, Value <: PolarG[ValueType],
    SelfD >: `BotD:` <: `TopD:`,
  ] => EntryDR[Key, ValueType, SelfD] => EntryGR[Key, Value, SelfD]
end EntryGR

sealed trait BundleGR[
  Keys >: NG[ArgsG] <: PG[ArgsG],
  SelfD >: `BotD:` <: `TopD:`,
]
object BundleGR:
  given BundleGR[PG[EmptyArgsG], `D0`[PD[EmptyRecordD]]]
  given [
    Key >: BotK <: TopK, Datatype >: `BotD:` <: `TopD:`,
    Head >: NG[LocRefG[Key, Datatype]] <: PG[LocRefG[Key, Datatype]], Tail >: NG[ArgsG] <: PG[ArgsG],
    TailD >: `BotD:` <: `TopRecordD:`,
    SelfD >: `BotD:` <: `TopD:`,
  ] => BundleGR[Tail, TailD] => NonEmptyRecordDR[
    Key, Datatype, TailD, SelfD,
  ] => BundleGR[PG[NonEmptyArgsG[Head, Tail]], SelfD]
end BundleGR

//endregion - `SelfD` Rules

//region ==== Other Rules ====

sealed trait ConcurrentGR[Processes >: NG[ArgsG] <: PG[ArgsG]]
object ConcurrentGR:
  given [
    ProcessesG <: ArgsG,
    Processes >: NG[ProcessesG] <: PG[ProcessesG],
  ] => BoundedArgs[ProcessesG, ApolarG] => ConcurrentGR[Processes]
end ConcurrentGR

sealed trait BoundedArgs[G <: ArgsG, Bound <: TopG]
object BoundedArgs:
  given [Bound <: TopG] => BoundedArgs[EmptyArgsG, Bound]
  given [
    TailG <: ArgsG,
    Head >: NG[Bound] <: PG[Bound], TailH >: NG[TailG] <: PG[TailG],
    Bound <: TopG,
  ] => BoundedArgs[TailG, Bound] => BoundedArgs[NonEmptyArgsG[Head, TailH], Bound]
end BoundedArgs

sealed trait SendGR[Contents <: ExprG[PD[TopD]], Recipient <: ExprG[PD[AddressD[TopK, ND[TopD]]]]]
object SendGR:
  given [
    Datatype >: ND[TopD] <: PD[TopD],
    Contents <: ExprG[Datatype], Recipient <: ExprG[PD[AddressD[TopK, Datatype]]],
  ] => SendGR[Contents, Recipient]
end SendGR

//endregion - Other Rules
