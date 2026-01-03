package fides.syntax

// -------------------------------------------------------------------------------------------------
// This file contains inductive rules for building more complex E (and E:) types from simpler ones.
// -------------------------------------------------------------------------------------------------

sealed trait NonEmptyRecordDR[
  -Key >: BotK <: TopK, -Value >: `BotE:` <: `TopE:`,
  -Tail >: `BotE:` <: `TopRecordE:`,
  SelfD >: `BotE:` <: `TopE:`,
]
sealed trait NonEmptyRecordLDR:
  given [
    Key >: BotK <: TopK, Value >: `-E`[TopD] <: `+E`[TopD], Tail >: `-E`[RecordD] <: `+E`[RecordD],
  ] => NonEmptyRecordDR[Key, `E+`[Value], `E+`[Tail], `E+`[`+E`[NonEmptyRecordD[Key, Value, Tail]]]]
  given [
    Key >: BotK <: TopK, Value >: `-E`[TopD] <: `+E`[TopD], Tail >: `-E`[RecordD] <: `+E`[RecordD],
  ] => NonEmptyRecordDR[Key, `E-`[Value], `E-`[Tail], `E-`[`+E`[NonEmptyRecordD[Key, Value, Tail]]]]
object NonEmptyRecordDR extends NonEmptyRecordLDR:
  given [
    Key >: BotK <: TopK, `Value+` >: `-E`[TopD] <: `+E`[TopD], `Value-` >: `-E`[TopD] <: `+E`[TopD],
    `Tail+` >: `-E`[RecordD] <: `+E`[RecordD], `Tail-` >: `-E`[RecordD] <: `+E`[RecordD],
  ] => NonEmptyRecordDR[
    Key, `E:`[`Value+`, `Value-`], `E:`[`Tail+`, `Tail-`],
    `E:`[`+E`[NonEmptyRecordD[Key, `Value+`, `Tail+`]], `+E`[NonEmptyRecordD[Key, `Value-`, `Tail-`]]],
  ]
end NonEmptyRecordDR

sealed trait EntryDR[
  Key >: BotK <: TopK, -Value >: `BotE:` <: `TopE:`,
  SelfD >: `BotE:` <: `TopE:`,
]
sealed trait EntryLDR:
  given [
    Key >: BotK <: TopK, Value >: `-E`[TopD] <: `+E`[TopD],
  ] => EntryDR[Key, `E+`[Value], `E+`[`+E`[EntryD[Key, Value]]]]
  given [
    Key >: BotK <: TopK, Value >: `-E`[TopD] <: `+E`[TopD],
  ] => EntryDR[Key, `E-`[Value], `E-`[`+E`[EntryD[Key, Value]]]]
object EntryDR extends EntryLDR:
  given [
    Key >: BotK <: TopK, `Value+` >: `-E`[TopD] <: `+E`[TopD], `Value-` >: `-E`[TopD] <: `+E`[TopD],
  ] => EntryDR[Key, `E:`[`Value+`, `Value-`], `E:`[`+E`[EntryD[Key, `Value+`]], `+E`[EntryD[Key, `Value-`]]]]
end EntryDR

sealed trait MergeBagsDR[
  Bag1 >: `-E`[BagD[`+E`[TopD]]] <: `+E`[BagD[`+E`[TopD]]],
  Bag2 >: `-E`[BagD[`+E`[TopD]]] <: `+E`[BagD[`+E`[TopD]]],
  Bag >: `-E`[BagD[`+E`[TopD]]] <: `+E`[BagD[`+E`[TopD]]],
]
sealed trait MergeBagsLDR:
  given [
    Bag1 >: `-E`[BagD[`+E`[TopD]]] <: `+E`[BagD[`+E`[TopD]]],
    Bag2 >: `-E`[BagD[`+E`[TopD]]] <: `+E`[BagD[`+E`[TopD]]],
    Bag >: `-E`[BagD[`+E`[TopD]]] <: `+E`[BagD[`+E`[TopD]]],
  ] => MergeBagsDR[Bag2, Bag1, Bag] => MergeBagsDR[Bag1, Bag2, Bag]
object MergeBagsDR extends MergeBagsLDR:
  given [Bag >: `-E`[BagD[`+E`[TopD]]] <: `+E`[BagD[`+E`[TopD]]]] => MergeBagsDR[Bag, `+E`[EmptyBagD], Bag]
  given [
    TailBound >: `-E`[TopD] <: `+E`[TopD],
    Tail >: `-E`[BagD[TailBound]] <: `+E`[BagD[TailBound]],
    Head1 >: `-E`[TopD] <: `+E`[TopD], Tail1 >: `-E`[BagD[`+E`[TopD]]] <: `+E`[BagD[`+E`[TopD]]],
    Head2 >: `-E`[TopD] <: `+E`[TopD],
    Bag1 >: `-E`[NonEmptyBagD[`+E`[TopD], Head1, Tail1]] <: `+E`[NonEmptyBagD[`+E`[TopD], Head1, Tail1]],
    Bag2 >: `-E`[
      NonEmptyBagD[`+E`[TopD], Head2, `+E`[BagD[`+E`[TopD]]]],
    ] <: `+E`[
      NonEmptyBagD[`+E`[TopD], Head2, `+E`[BagD[`+E`[TopD]]]],
    ],
  ] => (
    BeforeER[Head1, Head2],
    MergeBagsDR[Tail1, Bag2, Tail],
  ) => MergeBagsDR[Bag1, Bag2, `+E`[NonEmptyBagD[TailBound, Head1, Tail]]]
end MergeBagsDR
