package fides.syntax

// -------------------------------------------------------------------------------------------------
// This file contains inductive rules for building more complex D (and D:) types from simpler ones.
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

sealed trait EntryDR[
  Key >: BotK <: TopK, -Value >: `BotD:` <: `TopD:`,
  SelfD >: `BotD:` <: `TopD:`,
]
sealed trait EntryLDR:
  given [Key >: BotK <: TopK, Value >: BotD <: TopD] => EntryDR[Key, `D+`[Value], `D+`[EntryD[Key, Value]]]
  given [Key >: BotK <: TopK, Value >: BotD <: TopD] => EntryDR[Key, `D-`[Value], `D-`[EntryD[Key, Value]]]
object EntryDR extends EntryLDR:
  given [
    Key >: BotK <: TopK, `Value+` >: BotD <: TopD, `Value-` >: BotD <: TopD,
  ] => EntryDR[Key, `D:`[`Value+`, `Value-`], `D:`[EntryD[Key, `Value+`], EntryD[Key, `Value-`]]]
end EntryDR

sealed trait MergeBagsDR[Bag1 >: `-BagD` <: BagD[TopD], Bag2 >: `-BagD` <: BagD[TopD], Bag >: `-BagD` <: BagD[TopD]]
sealed trait MergeBagsLDR:
  given [
    Bag1 >: `-BagD` <: BagD[TopD], Bag2 >: `-BagD` <: BagD[TopD], Bag >: `-BagD` <: BagD[TopD],
  ] => MergeBagsDR[Bag2, Bag1, Bag] => MergeBagsDR[Bag1, Bag2, Bag]
object MergeBagsDR extends MergeBagsLDR:
  given [Bag >: `-BagD` <: BagD[TopD]] => MergeBagsDR[Bag, EmptyBagD, Bag]
  given [
    TailBound >: BotD <: TopD,
    Tail >: `-BagD` <: BagD[TailBound],
    Head1 >: BotD <: TopD, Tail1 >: `-BagD` <: BagD[TopD], Head2 >: BotD <: TopD,
    Bag1 >: `-BagD` <: NonEmptyBagD[TopD, Head1, Tail1],
    Bag2 >: `-BagD` <: NonEmptyBagD[TopD, Head2, ?],
  ] => (
    BeforeDR[Head1, Head2],
    MergeBagsDR[Tail1, Bag2, Tail],
  ) => MergeBagsDR[Bag1, Bag2, NonEmptyBagD[TailBound, Head1, Tail]]
end MergeBagsDR
