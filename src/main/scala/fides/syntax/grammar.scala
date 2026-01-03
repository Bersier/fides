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
final abstract class NonEmptyArgsG[+Head>: NG[TopG] <: PG[TopG], +Tail >: NG[ArgsG] <: PG[ArgsG]] extends ArgsG

final abstract class NameG[+K <: TopK] extends TopG
type LauncherNameG = NameG[LauncherK]

//region ==== Locations ====

/**
  * Helper type. Provides closure under union and intersection.
  */
private sealed trait CellHG[
  +K >: BotK <: TopK, +Datatype >: `BotD:` <: `TopD:`, +Value >: ND[TopD] <: PD[TopD],
] extends TopG
// todo I think we need to make this the main actual type, for the cases when we want to have a quote polar containing
//  Cell, with an escape in the position of the datatype.

type CellG[
  +K >: BotK <: TopK, Datatype >: ND[TopD] <: PD[TopD], +Value >: ND[TopD] <: Datatype,
] = CellHG[K, `D0`[Datatype], Value]

sealed trait LocRefG[+K <: TopK, +Datatype >: `BotD:` <: `TopD:`] extends TopG
sealed trait ChanRefG[+K <: TopK, +Datatype >: `BotD:` <: `TopD:`] extends LocRefG[K, Datatype]
sealed trait CellRefG[+K <: TopK, +Datatype >: `BotD:` <: `TopD:`] extends LocRefG[K, Datatype]

//endregion - Locations

//region ==== Xpolar Categories ====

sealed trait XpolarG extends TopG

sealed trait ApolarG extends XpolarG

sealed trait PolarG[+D >: `BotD:` <: `TopD:`] extends XpolarG
type ExprG[+D >: ND[TopD] <: PD[TopD]] = PolarG[`D+`[D]]
type XctrG[-D >: ND[TopD] <: PD[TopD]] = PolarG[`D-`[D]]

final abstract class BipolarG[+I >: `BotD::` <: `TopD::`, +O >: `BotD::` <: `TopD::`] extends XpolarG

//endregion - Xpolar Categories

//region ==== Xpolar Converters ====

final abstract class BlockExprG[
  +D >: ND[TopD] <: PD[TopD],
  +Apolar >: NG[ApolarG] <: PG[ApolarG], +Expr >: NG[ExprG[D]] <: PG[ExprG[D]],
] extends ExprG[D]

final abstract class BlockXctrG[
  -D >: ND[TopD] <: PD[TopD],
  +Apolar >: NG[ApolarG] <: PG[ApolarG], +Xctr >: NG[XctrG[D]] <: PG[XctrG[D]],
] extends XctrG[D]

//endregion - Xpolar Converters

//region ==== Apolars ====

final abstract class ConcurrentG[+Processes >: NG[ArgsG] <: PG[ArgsG]] extends ApolarG

final abstract class RepeatedG[+Process >: NG[ApolarG] <: PG[ApolarG]] extends ApolarG

final abstract class SendG[
  +Contents >: NG[ExprG[PD[TopD]]] <: PG[ExprG[PD[TopD]]],
  +Recipient >: NG[ExprG[PD[AddressD[TopK, ND[TopD]]]]] <: PG[ExprG[PD[AddressD[TopK, ND[TopD]]]]],
] extends ApolarG

final abstract class DivModG[
  +Dividend >: NG[ExprG[PD[NatD[TopN]]]] <: PG[ExprG[PD[NatD[TopN]]]],
  +Divisor >: NG[ExprG[PD[NatD[PosN]]]] <: PG[ExprG[PD[NatD[PosN]]]],
  +Quotient >: NG[XctrG[PD[NatD[TopN]]]] <: PG[XctrG[PD[NatD[TopN]]]],
  +Remainder >: NG[XctrG[PD[NatD[TopN]]]] <: PG[XctrG[PD[NatD[TopN]]]],
] extends ApolarG

//endregion - Apolars

//region ==== Constructor/Destructor Polars ====

sealed trait RecordG extends PolarG[`D0`[PD[RecordD]]]

final abstract class EmptyRecordG extends RecordG, PolarG[`D0`[PD[EmptyRecordD]]]

/**
  * Helper type. Provides closure under union and intersection.
  */
private final abstract class NonEmptyRecordHG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Key >: `BotK:` <: `TopK:`, +Value >: NG[PolarG[`TopD:`]] <: PG[PolarG[`TopD:`]],
  +Tail >: NG[RecordG] <: PG[RecordG],
] extends RecordG, PolarG[SelfD]

type NonEmptyRecordG[
  +SelfD >: `BotD:` <: `TopD:`,
  Key >: BotK <: TopK, +Value >: NG[PolarG[`TopD:`]] <: PG[PolarG[`TopD:`]],
  +Tail >: NG[RecordG] <: PG[RecordG],
] = NonEmptyRecordHG[SelfD, `K0`[Key], Value, Tail]

/**
  * Helper type. Provides closure under union and intersection.
  */
private final abstract class EntryHG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Key >: `BotK:` <: `TopK:`, +Value >: NG[PolarG[`TopD:`]] <: PG[PolarG[`TopD:`]],
] extends PolarG[SelfD]

type EntryG[
  +SelfD >: `BotD:` <: `TopD:`,
  Key >: BotK <: TopK, +Value >: NG[PolarG[`TopD:`]] <: PG[PolarG[`TopD:`]],
] = EntryHG[SelfD, `K0`[Key], Value]

final abstract class BagG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Elements >: NG[ArgsG] <: PG[ArgsG],
] extends PolarG[SelfD]

final abstract class PickG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Options >: NG[ArgsG] <: PG[ArgsG],
] extends PolarG[SelfD]

/**
  * Helper type. Provides closure under union and intersection.
  */
private final abstract class QuoteHG[
  +SelfD >: `BotD:` <: `TopD:`,
  +K >: `BotK:` <: `TopK:`, +Body >: BotM <: TopM,
] extends PolarG[SelfD]

type QuoteG[
  +SelfD >: `BotD:` <: `TopD:`,
  K >: BotK <: TopK, +Body >: BotM <: TopM,
] = QuoteHG[SelfD, `K0`[K], Body]

/**
  * Helper type. Provides closure under union and intersection.
  */
private final abstract class CertificateHG[
  +SelfD >: `BotD:` <: `TopD:`,
  +K >: `BotK:` <: `TopK:`, +Payload  >: NG[PolarG[`TopD:`]] <: PG[PolarG[`TopD:`]],
] extends PolarG[SelfD]

type CertificateG[
  +SelfD >: `BotD:` <: `TopD:`,
  K >: BotK <: TopK, +Payload >: NG[PolarG[`TopD:`]] <: PG[PolarG[`TopD:`]],
] = CertificateHG[SelfD, `K0`[K], Payload]

/**
  * Helper type. Provides closure under union and intersection.
  */
private final abstract class IdentifierHG[
  +SelfD >: `BotD:` <: `TopD:`,
  +K >: `BotK:` <: `TopK:`,
] extends PolarG[SelfD]

type IdentifierG[K >: BotK <: TopK] = IdentifierHG[`D0`[PD[IdentifierD[K]]], `K0`[K]]

final abstract class AddressG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Name >: NG[IdentifierPolarG] <: PG[IdentifierPolarG],
  -Datatype >: `BotD:` <: `TopD:`,
] extends PolarG[SelfD]

final abstract class NatG[
  +SelfD >: `BotD:` <: `TopD:`,
  +N >: `BotN:` <: `TopN:`,
] extends PolarG[SelfD]

final abstract class KillG extends PolarG[`D0`[PD[KillD]]]
final abstract class PauseG extends PolarG[`D0`[PD[PauseD]]]
final abstract class StartG extends PolarG[`D0`[PD[StartD]]]

final abstract class FalseG extends PolarG[`D0`[PD[FalseD]]]
final abstract class TrueG extends PolarG[`D0`[PD[TrueD]]]

final abstract class PulseG extends PolarG[`D0`[PD[PulseD]]]

//endregion - Constructor/Destructor Polars

//region ==== Other Reversible Polars ====

private final abstract class BundleG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Keys >: NG[ArgsG] <: PG[ArgsG],
] extends PolarG[SelfD]

private final abstract class SwitchG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Keys >: NG[ArgsG] <: PG[ArgsG],
] extends PolarG[SelfD]

/**
  * Helper type. Provides closure under union and intersection.
  */
private final abstract class CollectHG[
  +SelfD >: `BotD:` <: `TopD:`,
  +K >: `BotK:` <: `TopK:`, +ElementType >: `BotD:` <: `TopD:`,
  +Size >: NG[NatPolarG] <: PG[NatPolarG],
] extends PolarG[SelfD]

type CollectG[
  +SelfD >: `BotD:` <: `TopD:`,
  K >: BotK <: TopK, +ElementType >: `BotD:` <: `TopD:`,
  +Size >: NG[NatPolarG] <: PG[NatPolarG],
] = CollectHG[SelfD, `K0`[K], ElementType, Size]

final abstract class NegateG[
  +SelfD >: `BotD:` <: `TopD:`,
  +B >: NG[BoolPolarG] <: PG[BoolPolarG],
] extends PolarG[SelfD]

/**
  * Helper type. Provides closure under union and intersection.
  */
private final abstract class SwapHG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Target >: `BotK:` <: `TopK:`, +Replacement >: `BotK:` <: `TopK:`,
  +Object >: NG[PolarG[`TopD:`]] <: PG[PolarG[`TopD:`]],
]

type SwapG[
  +SelfD >: `BotD:` <: `TopD:`,
  Target >: BotK <: TopK, Replacement >: BotK <: TopK,
  +Object >: NG[PolarG[`TopD:`]] <: PG[PolarG[`TopD:`]],
] = SwapHG[SelfD, `K0`[Target], `K0`[Replacement], Object]

//endregion - Other Reversible Polars

//region ==== Other Expression Polars ====

final abstract class DeShadowFreshenG[
  +SelfD >: ND[TopD] <: PD[TopD],
  +Quote >: NG[ExprG[PD[QuoteD[TopM]]]] <: PG[ExprG[PD[QuoteD[TopM]]]],
] extends ExprG[SelfD]

final abstract class MergeBagsG[
  +Bag1Type >: ND[BagD[PD[TopD]]] <: PD[BagD[PD[TopD]]],
  +Bag2Type >: ND[BagD[PD[TopD]]] <: PD[BagD[PD[TopD]]],
  +BagType >: ND[BagD[PD[TopD]]] <: PD[BagD[PD[TopD]]],
  +Bag1  >: NG[ExprG[Bag1Type]] <: PG[ExprG[Bag1Type]],
  +Bag2  >: NG[ExprG[Bag2Type]] <: PG[ExprG[Bag2Type]],
] extends ExprG[BagType]

final abstract class AddG[+Terms >: NG[ArgsG] <: PG[ArgsG]] extends ExprG[PD[NatD[TopN]]]

final abstract class MultiplyG[+Factors >: NG[ArgsG] <: PG[ArgsG]] extends ExprG[PD[NatD[TopN]]]

final abstract class ConjoinG[+Conjuncts >: NG[ArgsG] <: PG[ArgsG]] extends ExprG[PD[BoolD]]

final abstract class DisjoinG[+Disjuncts >: NG[ArgsG] <: PG[ArgsG]] extends ExprG[PD[BoolD]]

//endregion - Other Expression Polars

//region ==== Other Extractor Polars ====

final abstract class InspectG[
  -PayloadType >: ND[TopD] <: PD[TopD],
  +Signature >: NG[XctrG[PD[IdentifierD[TopK]]]] <: PG[XctrG[PD[IdentifierD[TopK]]]],
  +Payload >: NG[XctrG[PayloadType]] <: PG[XctrG[PayloadType]],
] extends XctrG[PD[CertificateD[TopK, PayloadType]]]

//endregion - Other Extractor Polars

//region ==== Synonyms ====

type IdentifierPolarG =
  PolarG[`D:`[PD[IdentifierD[TopK]], ND[OffTopD]] | `D:`[PD[OffTopD], PD[IdentifierD[BotK]]]]
type BoolPolarG = PolarG[`D:`[PD[BoolD], ND[OffTopD]] | `D:`[PD[OffTopD], ND[BoolD]]]
type NatPolarG = PolarG[`D:`[PD[NatD[TopN]], ND[OffTopD]] | `D:`[PD[OffTopD], PD[NatD[BotN]]]]

//endregion - Synonyms
