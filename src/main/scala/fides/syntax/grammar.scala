package fides.syntax

// -------------------------------------------------------------------------------------------------
// This file contains the constructors to build a full characterization of any piece of Fides code,
// except for top-level escapes.
//
// Additional grammar rules are in `grules.scala`.
// -------------------------------------------------------------------------------------------------

sealed trait OffTopG private[syntax]()

sealed trait TopG extends OffTopG

/**
  * An unordered list of Gs
  *
  * Because we cannot represent unordered types in Scala, we instead use a list of G types.
  * It is assumed that it is sorted by G type, so it's a canonical representation of the G multiset.
  */
sealed trait ArgsG extends TopG
final abstract class EmptyArgsG extends ArgsG
final abstract class NonEmptyArgsG[+Head>: `-H`[TopG] <: `+H`[TopG], +Tail >: `-H`[ArgsG] <: `+H`[ArgsG]] extends ArgsG

final abstract class NameG[+K <: TopK] extends TopG
type LauncherNameG = NameG[LauncherK]

//region ==== Locations ====

/**
  * Helper type. Provides closure under union and intersection.
  */
private sealed trait CellHG[
  +K >: BotK <: TopK, +Datatype >: `BotE:` <: `TopE:`, +Value >: `-E`[TopD] <: `+E`[TopD],
] extends TopG
// todo I think we need to make this the main actual type, for the cases when we want to have a quote polar containing
//  Cell, with an escape in the position of the datatype.

type CellG[
  +K >: BotK <: TopK, Datatype >: `-E`[TopD] <: `+E`[TopD], +Value >: `-E`[TopD] <: Datatype,
] = CellHG[K, `E0`[Datatype], Value]

sealed trait LocRefG[+K <: TopK, +Datatype >: `BotE:` <: `TopE:`] extends TopG
sealed trait ChanRefG[+K <: TopK, +Datatype >: `BotE:` <: `TopE:`] extends LocRefG[K, Datatype]
sealed trait CellRefG[+K <: TopK, +Datatype >: `BotE:` <: `TopE:`] extends LocRefG[K, Datatype]

//endregion - Locations

//region ==== Xpolar Categories ====

sealed trait XpolarG extends TopG

sealed trait ApolarG extends XpolarG

sealed trait PolarG[+D >: `BotE:` <: `TopE:`] extends XpolarG
type ExprG[+D >: `-E`[TopD] <: `+E`[TopD]] = PolarG[`E+`[D]]
type XctrG[-D >: `-E`[TopD] <: `+E`[TopD]] = PolarG[`E-`[D]]

final abstract class BipolarG[+I >: `BotE::` <: `TopE::`, +O >: `BotE::` <: `TopE::`] extends XpolarG

//endregion - Xpolar Categories

//region ==== Xpolar Converters ====

final abstract class BlockExprG[
  +D >: `-E`[TopD] <: `+E`[TopD],
  +Apolar >: `-H`[ApolarG] <: `+H`[ApolarG], +Expr >: `-H`[ExprG[D]] <: `+H`[ExprG[D]],
] extends ExprG[D]

final abstract class BlockXctrG[
  -D >: `-E`[TopD] <: `+E`[TopD],
  +Apolar >: `-H`[ApolarG] <: `+H`[ApolarG], +Xctr >: `-H`[XctrG[D]] <: `+H`[XctrG[D]],
] extends XctrG[D]

//endregion - Xpolar Converters

//region ==== Apolars ====

final abstract class ConcurrentG[+Processes >: `-H`[ArgsG] <: `+H`[ArgsG]] extends ApolarG

final abstract class RepeatedG[+Process >: `-H`[ApolarG] <: `+H`[ApolarG]] extends ApolarG

final abstract class SendG[
  +Contents >: `-H`[ExprG[`+E`[TopD]]] <: `+H`[ExprG[`+E`[TopD]]],
  +Recipient >: `-H`[ExprG[`+E`[AddressD[TopK, `-E`[TopD]]]]] <: `+H`[ExprG[`+E`[AddressD[TopK, `-E`[TopD]]]]],
] extends ApolarG

final abstract class DivModG[
  +Dividend >: `-H`[ExprG[`+E`[NatD[TopN]]]] <: `+H`[ExprG[`+E`[NatD[TopN]]]],
  +Divisor >: `-H`[ExprG[`+E`[NatD[PosN]]]] <: `+H`[ExprG[`+E`[NatD[PosN]]]],
  +Quotient >: `-H`[XctrG[`+E`[NatD[TopN]]]] <: `+H`[XctrG[`+E`[NatD[TopN]]]],
  +Remainder >: `-H`[XctrG[`+E`[NatD[TopN]]]] <: `+H`[XctrG[`+E`[NatD[TopN]]]],
] extends ApolarG

//endregion - Apolars

//region ==== Constructor/Destructor Polars ====

sealed trait RecordG extends PolarG[`E0`[`+E`[RecordD]]]

final abstract class EmptyRecordG extends RecordG, PolarG[`E0`[`+E`[EmptyRecordD]]]

/**
  * Helper type. Provides closure under union and intersection.
  */
private final abstract class NonEmptyRecordHG[
  +SelfD >: `BotE:` <: `TopE:`,
  +Key >: `BotK:` <: `TopK:`, +Value >: `-H`[PolarG[`TopE:`]] <: `+H`[PolarG[`TopE:`]],
  +Tail >: `-H`[RecordG] <: `+H`[RecordG],
] extends RecordG, PolarG[SelfD]

type NonEmptyRecordG[
  +SelfD >: `BotE:` <: `TopE:`,
  Key >: BotK <: TopK, +Value >: `-H`[PolarG[`TopE:`]] <: `+H`[PolarG[`TopE:`]],
  +Tail >: `-H`[RecordG] <: `+H`[RecordG],
] = NonEmptyRecordHG[SelfD, `K0`[Key], Value, Tail]

/**
  * Helper type. Provides closure under union and intersection.
  */
private final abstract class EntryHG[
  +SelfD >: `BotE:` <: `TopE:`,
  +Key >: `BotK:` <: `TopK:`, +Value >: `-H`[PolarG[`TopE:`]] <: `+H`[PolarG[`TopE:`]],
] extends PolarG[SelfD]

type EntryG[
  +SelfD >: `BotE:` <: `TopE:`,
  Key >: BotK <: TopK, +Value >: `-H`[PolarG[`TopE:`]] <: `+H`[PolarG[`TopE:`]],
] = EntryHG[SelfD, `K0`[Key], Value]

final abstract class BagG[
  +SelfD >: `BotE:` <: `TopE:`,
  +Elements >: `-H`[ArgsG] <: `+H`[ArgsG],
] extends PolarG[SelfD]

final abstract class PickG[
  +SelfD >: `BotE:` <: `TopE:`,
  +Options >: `-H`[ArgsG] <: `+H`[ArgsG],
] extends PolarG[SelfD]

/**
  * Helper type. Provides closure under union and intersection.
  */
private final abstract class QuoteHG[
  +SelfD >: `BotE:` <: `TopE:`,
  +K >: `BotK:` <: `TopK:`, +Body >: BotM <: TopM,
] extends PolarG[SelfD]

type QuoteG[
  +SelfD >: `BotE:` <: `TopE:`,
  K >: BotK <: TopK, +Body >: BotM <: TopM,
] = QuoteHG[SelfD, `K0`[K], Body]

/**
  * Helper type. Provides closure under union and intersection.
  */
private final abstract class CertificateHG[
  +SelfD >: `BotE:` <: `TopE:`,
  +K >: `BotK:` <: `TopK:`, +Payload  >: `-H`[PolarG[`TopE:`]] <: `+H`[PolarG[`TopE:`]],
] extends PolarG[SelfD]

type CertificateG[
  +SelfD >: `BotE:` <: `TopE:`,
  K >: BotK <: TopK, +Payload >: `-H`[PolarG[`TopE:`]] <: `+H`[PolarG[`TopE:`]],
] = CertificateHG[SelfD, `K0`[K], Payload]

/**
  * Helper type. Provides closure under union and intersection.
  */
private final abstract class IdentifierHG[
  +SelfD >: `BotE:` <: `TopE:`,
  +K >: `BotK:` <: `TopK:`,
] extends PolarG[SelfD]

type IdentifierG[K >: BotK <: TopK] = IdentifierHG[`E0`[`+E`[IdentifierD[K]]], `K0`[K]]

final abstract class AddressG[
  +SelfD >: `BotE:` <: `TopE:`,
  +Name >: `-H`[IdentifierPolarG] <: `+H`[IdentifierPolarG],
  -Datatype >: `BotE:` <: `TopE:`,
] extends PolarG[SelfD]

final abstract class NatG[
  +SelfD >: `BotE:` <: `TopE:`,
  +N >: `BotN:` <: `TopN:`,
] extends PolarG[SelfD]

final abstract class KillG extends PolarG[`E0`[`+E`[KillD]]]
final abstract class PauseG extends PolarG[`E0`[`+E`[PauseD]]]
final abstract class StartG extends PolarG[`E0`[`+E`[StartD]]]

final abstract class FalseG extends PolarG[`E0`[`+E`[FalseD]]]
final abstract class TrueG extends PolarG[`E0`[`+E`[TrueD]]]

final abstract class PulseG extends PolarG[`E0`[`+E`[PulseD]]]

//endregion - Constructor/Destructor Polars

//region ==== Other Reversible Polars ====

private final abstract class BundleG[
  +SelfD >: `BotE:` <: `TopE:`,
  +Keys >: `-H`[ArgsG] <: `+H`[ArgsG],
] extends PolarG[SelfD]

private final abstract class SwitchG[
  +SelfD >: `BotE:` <: `TopE:`,
  +Keys >: `-H`[ArgsG] <: `+H`[ArgsG],
] extends PolarG[SelfD]

/**
  * Helper type. Provides closure under union and intersection.
  */
private final abstract class CollectHG[
  +SelfD >: `BotE:` <: `TopE:`,
  +K >: `BotK:` <: `TopK:`, +ElementType >: `BotE:` <: `TopE:`,
  +Size >: `-H`[NatPolarG] <: `+H`[NatPolarG],
] extends PolarG[SelfD]

type CollectG[
  +SelfD >: `BotE:` <: `TopE:`,
  K >: BotK <: TopK, +ElementType >: `BotE:` <: `TopE:`,
  +Size >: `-H`[NatPolarG] <: `+H`[NatPolarG],
] = CollectHG[SelfD, `K0`[K], ElementType, Size]

final abstract class NegateG[
  +SelfD >: `BotE:` <: `TopE:`,
  +B >: `-H`[BoolPolarG] <: `+H`[BoolPolarG],
] extends PolarG[SelfD]

/**
  * Helper type. Provides closure under union and intersection.
  */
private final abstract class SwapHG[
  +SelfD >: `BotE:` <: `TopE:`,
  +Target >: `BotK:` <: `TopK:`, +Replacement >: `BotK:` <: `TopK:`,
  +Object >: `-H`[PolarG[`TopE:`]] <: `+H`[PolarG[`TopE:`]],
]

type SwapG[
  +SelfD >: `BotE:` <: `TopE:`,
  Target >: BotK <: TopK, Replacement >: BotK <: TopK,
  +Object >: `-H`[PolarG[`TopE:`]] <: `+H`[PolarG[`TopE:`]],
] = SwapHG[SelfD, `K0`[Target], `K0`[Replacement], Object]

//endregion - Other Reversible Polars

//region ==== Other Expression Polars ====

final abstract class DeShadowFreshenG[
  +SelfD >: `-E`[TopD] <: `+E`[TopD],
  +Quote >: `-H`[ExprG[`+E`[QuoteD[TopM]]]] <: `+H`[ExprG[`+E`[QuoteD[TopM]]]],
] extends ExprG[SelfD]

final abstract class MergeBagsG[
  +Bag1Type >: `-E`[BagD[`+E`[TopD]]] <: `+E`[BagD[`+E`[TopD]]],
  +Bag2Type >: `-E`[BagD[`+E`[TopD]]] <: `+E`[BagD[`+E`[TopD]]],
  +BagType >: `-E`[BagD[`+E`[TopD]]] <: `+E`[BagD[`+E`[TopD]]],
  +Bag1  >: `-H`[ExprG[Bag1Type]] <: `+H`[ExprG[Bag1Type]],
  +Bag2  >: `-H`[ExprG[Bag2Type]] <: `+H`[ExprG[Bag2Type]],
] extends ExprG[BagType]

final abstract class AddG[+Terms >: `-H`[ArgsG] <: `+H`[ArgsG]] extends ExprG[`+E`[NatD[TopN]]]

final abstract class MultiplyG[+Factors >: `-H`[ArgsG] <: `+H`[ArgsG]] extends ExprG[`+E`[NatD[TopN]]]

final abstract class ConjoinG[+Conjuncts >: `-H`[ArgsG] <: `+H`[ArgsG]] extends ExprG[`+E`[BoolD]]

final abstract class DisjoinG[+Disjuncts >: `-H`[ArgsG] <: `+H`[ArgsG]] extends ExprG[`+E`[BoolD]]

//endregion - Other Expression Polars

//region ==== Other Extractor Polars ====

final abstract class InspectG[
  -PayloadType >: `-E`[TopD] <: `+E`[TopD],
  +Signature >: `-H`[XctrG[`+E`[IdentifierD[TopK]]]] <: `+H`[XctrG[`+E`[IdentifierD[TopK]]]],
  +Payload >: `-H`[XctrG[PayloadType]] <: `+H`[XctrG[PayloadType]],
] extends XctrG[`+E`[CertificateD[TopK, PayloadType]]]

//endregion - Other Extractor Polars

//region ==== Synonyms ====

type IdentifierPolarG =
  PolarG[`E:`[`+E`[IdentifierD[TopK]], `-E`[OffTopD]] | `E:`[`+E`[OffTopD], `+E`[IdentifierD[BotK]]]]
type BoolPolarG = PolarG[`E:`[`+E`[BoolD], `-E`[OffTopD]] | `E:`[`+E`[OffTopD], `-E`[BoolD]]]
type NatPolarG = PolarG[`E:`[`+E`[NatD[TopN]], `-E`[OffTopD]] | `E:`[`+E`[OffTopD], `+E`[NatD[BotN]]]]

//endregion - Synonyms
