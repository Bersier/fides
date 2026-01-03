package fides.syntax

// -------------------------------------------------------------------------------------------------
// This file contains inductive rules for building more complex D (and D:) types from simpler ones.
// -------------------------------------------------------------------------------------------------

sealed trait NonEmptyRecordDR[
  -Key >: BotK <: TopK, -Value >: `BotD:` <: `TopD:`,
  -Tail >: `BotD:` <: `TopRecordD:`,
  SelfD >: `BotD:` <: `TopD:`,
]
sealed trait NonEmptyRecordLDR:
  given [
    Key >: BotK <: TopK, Value >: ND[TopD] <: PD[TopD], Tail >: ND[RecordD] <: PD[RecordD],
  ] => NonEmptyRecordDR[Key, `D+`[Value], `D+`[Tail], `D+`[PD[NonEmptyRecordD[Key, Value, Tail]]]]
  given [
    Key >: BotK <: TopK, Value >: ND[TopD] <: PD[TopD], Tail >: ND[RecordD] <: PD[RecordD],
  ] => NonEmptyRecordDR[Key, `D-`[Value], `D-`[Tail], `D-`[PD[NonEmptyRecordD[Key, Value, Tail]]]]
object NonEmptyRecordDR extends NonEmptyRecordLDR:
  given [
    Key >: BotK <: TopK, `Value+` >: ND[TopD] <: PD[TopD], `Value-` >: ND[TopD] <: PD[TopD],
    `Tail+` >: ND[RecordD] <: PD[RecordD], `Tail-` >: ND[RecordD] <: PD[RecordD],
  ] => NonEmptyRecordDR[
    Key, `D:`[`Value+`, `Value-`], `D:`[`Tail+`, `Tail-`],
    `D:`[PD[NonEmptyRecordD[Key, `Value+`, `Tail+`]], PD[NonEmptyRecordD[Key, `Value-`, `Tail-`]]],
  ]
end NonEmptyRecordDR

sealed trait EntryDR[
  Key >: BotK <: TopK, -Value >: `BotD:` <: `TopD:`,
  SelfD >: `BotD:` <: `TopD:`,
]
sealed trait EntryLDR:
  given [
    Key >: BotK <: TopK, Value >: ND[TopD] <: PD[TopD],
  ] => EntryDR[Key, `D+`[Value], `D+`[PD[EntryD[Key, Value]]]]
  given [
    Key >: BotK <: TopK, Value >: ND[TopD] <: PD[TopD],
  ] => EntryDR[Key, `D-`[Value], `D-`[PD[EntryD[Key, Value]]]]
object EntryDR extends EntryLDR:
  given [
    Key >: BotK <: TopK, `Value+` >: ND[TopD] <: PD[TopD], `Value-` >: ND[TopD] <: PD[TopD],
  ] => EntryDR[Key, `D:`[`Value+`, `Value-`], `D:`[PD[EntryD[Key, `Value+`]], PD[EntryD[Key, `Value-`]]]]
end EntryDR

sealed trait MergeBagsDR[
  Bag1 >: ND[BagD[PD[TopD]]] <: PD[BagD[PD[TopD]]],
  Bag2 >: ND[BagD[PD[TopD]]] <: PD[BagD[PD[TopD]]],
  Bag >: ND[BagD[PD[TopD]]] <: PD[BagD[PD[TopD]]],
]
sealed trait MergeBagsLDR:
  given [
    Bag1 >: ND[BagD[PD[TopD]]] <: PD[BagD[PD[TopD]]],
    Bag2 >: ND[BagD[PD[TopD]]] <: PD[BagD[PD[TopD]]],
    Bag >: ND[BagD[PD[TopD]]] <: PD[BagD[PD[TopD]]],
  ] => MergeBagsDR[Bag2, Bag1, Bag] => MergeBagsDR[Bag1, Bag2, Bag]
object MergeBagsDR extends MergeBagsLDR:
  given [Bag >: ND[BagD[PD[TopD]]] <: PD[BagD[PD[TopD]]]] => MergeBagsDR[Bag, PD[EmptyBagD], Bag]
  given [
    TailBound >: ND[TopD] <: PD[TopD],
    Tail >: ND[BagD[TailBound]] <: PD[BagD[TailBound]],
    Head1 >: ND[TopD] <: PD[TopD], Tail1 >: ND[BagD[PD[TopD]]] <: PD[BagD[PD[TopD]]],
    Head2 >: ND[TopD] <: PD[TopD],
    Bag1 >: ND[NonEmptyBagD[PD[TopD], Head1, Tail1]] <: PD[NonEmptyBagD[PD[TopD], Head1, Tail1]],
    Bag2 >: ND[
      NonEmptyBagD[PD[TopD], Head2, PD[BagD[PD[TopD]]]],
    ] <: PD[
      NonEmptyBagD[PD[TopD], Head2, PD[BagD[PD[TopD]]]],
    ],
  ] => (
    BeforeER[Head1, Head2],
    MergeBagsDR[Tail1, Bag2, Tail],
  ) => MergeBagsDR[Bag1, Bag2, PD[NonEmptyBagD[TailBound, Head1, Tail]]]
end MergeBagsDR
