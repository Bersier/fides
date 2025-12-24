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
final abstract class NonEmptyArgsG[+Head >: BotH <: TopH, +Tail >: `-H`[ArgsG] <: `+H`[ArgsG]] extends ArgsG

//region ==== Locations ====

final abstract class LocG[+K >: BotK <: TopK, +Datatype >: `BotD:` <: `TopD:`] extends TopG

//endregion - Locations

//region ==== Xpolar Categories ====

sealed trait XpolarG extends TopG

sealed trait ApolarG extends XpolarG

sealed trait PolarG[+D >: `BotD:` <: `TopD:`] extends XpolarG
type ExprG[+D >: BotD <: TopD] = PolarG[`D+`[D]]
type XctrG[-D >: BotD <: TopD] = PolarG[`D-`[D]]

final abstract class BipolarG[+I >: `BotD::` <: `TopD::`, +O >: `BotD::` <: `TopD::`] extends XpolarG

//endregion - Xpolar Categories

//region ==== Apolars ====

final abstract class ConcurrentG[+Processes >: `-H`[ArgsG] <: `+H`[ArgsG]] extends ApolarG

final abstract class RepeatedG[+Process >: `-H`[ApolarG] <: `+H`[ApolarG]] extends ApolarG

final abstract class SendG[
  +Contents >: `-H`[ExprG[TopD]] <: `+H`[ExprG[TopD]],
  +Recipient >: `-H`[ExprG[AddressD[TopK, BotD]]] <: `+H`[ExprG[AddressD[TopK, BotD]]],
] extends ApolarG

//endregion - Apolars

//region ==== Constructor/Destructor Polars ====

sealed trait RecordG extends PolarG[`D0`[RecordD]]

final abstract class EmptyRecordG extends RecordG, PolarG[`D0`[EmptyRecordD]]

/**
  * Helper type. It helps with being able to take the lower bound over [[NonEmptyRecordG]],
  * even though the latter is invariant in [[K]].
  */
private final abstract class NonEmptyRecordHG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Key >: `BotK:` <: `TopK:`, +Value >: `-H`[PolarG[`TopD:`]] <: `+H`[PolarG[`TopD:`]],
  +Tail >: `-H`[RecordG] <: `+H`[RecordG],
] extends RecordG, PolarG[SelfD]

type NonEmptyRecordG[
  +SelfD >: `BotD:` <: `TopD:`,
  Key >: BotK <: TopK, +Value >: `-H`[PolarG[`TopD:`]] <: `+H`[PolarG[`TopD:`]],
  +Tail >: `-H`[RecordG] <: `+H`[RecordG],
] = NonEmptyRecordHG[SelfD, `K0`[Key], Value, Tail]

/**
  * Helper type. It helps with being able to take the lower bound over [[VariantG]],
  * even though the latter is invariant in [[K]].
  */
private final abstract class VariantHG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Key >: `BotK:` <: `TopK:`, +Value >: `-H`[PolarG[`TopD:`]] <: `+H`[PolarG[`TopD:`]],
] extends PolarG[SelfD]

type VariantG[
  +SelfD >: `BotD:` <: `TopD:`,
  Key >: BotK <: TopK, +Value >: `-H`[PolarG[`TopD:`]] <: `+H`[PolarG[`TopD:`]],
] = VariantHG[SelfD, `K0`[Key], Value]

final abstract class MultisetG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Elements >: `-H`[ArgsG] <: `+H`[ArgsG],
] extends PolarG[SelfD]

final abstract class BagG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Elements >: `-H`[ArgsG] <: `+H`[ArgsG],
] extends PolarG[SelfD]

/**
  * Helper type. It helps with being able to take the lower bound over [[QuoteG]],
  * even though the latter is invariant in [[K]].
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
  * Helper type. It helps with being able to take the lower bound over [[CertificateG]],
  * even though the latter is invariant in [[K]].
  */
private final abstract class CertificateHG[
  +SelfD >: `BotD:` <: `TopD:`,
  +K >: `BotK:` <: `TopK:`, +Payload  >: `-H`[PolarG[`TopD:`]] <: `+H`[PolarG[`TopD:`]],
] extends PolarG[SelfD]

type CertificateG[
  +SelfD >: `BotD:` <: `TopD:`,
  K >: BotK <: TopK, +Payload  >: `-H`[PolarG[`TopD:`]] <: `+H`[PolarG[`TopD:`]],
] = CertificateHG[SelfD, `K0`[K], Payload]

/**
  * Helper type. It helps with being able to take the lower bound over [[IdentifierG]],
  * even though the latter is invariant in [[K]].
  */
private final abstract class IdentifierHG[
  +SelfD >: `BotD:` <: `TopD:`,
  +K >: `BotK:` <: `TopK:`,
] extends PolarG[SelfD]

type IdentifierG[K >: BotK <: TopK] = IdentifierHG[`D0`[IdentifierD[K]], `K0`[K]]

final abstract class AddressG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Name >: `-H`[IdentifierPolarG] <: `+H`[IdentifierPolarG],
  -Datatype >: `BotD:` <: `TopD:`,
] extends PolarG[SelfD]

final abstract class NatG[
  +SelfD >: `BotD:` <: `TopD:`,
  +N >: `BotN:` <: `TopN:`,
] extends PolarG[SelfD]

final abstract class KillG extends PolarG[`D0`[KillD]]
final abstract class PauseG extends PolarG[`D0`[PauseD]]
final abstract class StartG extends PolarG[`D0`[StartD]]

final abstract class FalseG extends PolarG[`D0`[FalseD]]
final abstract class TrueG extends PolarG[`D0`[TrueD]]

final abstract class PulseG extends PolarG[`D0`[PulseD]]

//endregion - Constructor/Destructor Polars

//region ==== Other Reversible Polars ====

/**
  * Helper type. It helps with being able to take the lower bound over [[CollectG]],
  * even though the latter is invariant in [[K]].
  */
private final abstract class CollectHG[
  +SelfD >: `BotD:` <: `TopD:`,
  +K >: `BotK:` <: `TopK:`, +ElementType >: `BotD:` <: `TopD:`,
  +Size >: `-H`[NatPolarG] <: `+H`[NatPolarG],
] extends PolarG[SelfD]

type CollectG[
  +SelfD >: `BotD:` <: `TopD:`,
  K >: BotK <: TopK, +ElementType >: `BotD:` <: `TopD:`,
  +Size >: `-H`[NatPolarG] <: `+H`[NatPolarG],
] = CollectHG[SelfD, `K0`[K], ElementType, Size]

final abstract class NegateG[
  +SelfD >: `BotD:` <: `TopD:`,
  +B >: `-H`[BoolPolarG] <: `+H`[BoolPolarG],
] extends PolarG[SelfD]

//endregion - Other Reversible Polars

//region ==== Other Expression Polars ====

final abstract class MergeBagsG[
  +ElementType >: BotD <: TopD,
  +Bag1  >: `-H`[ExprG[BagD[ElementType]]] <: `+H`[ExprG[BagD[ElementType]]],
  +Bag2  >: `-H`[ExprG[BagD[ElementType]]] <: `+H`[ExprG[BagD[ElementType]]],
] extends ExprG[BagD[ElementType]]

final abstract class AddG[+Terms >: `-H`[ArgsG] <: `+H`[ArgsG]] extends ExprG[NatD[TopN]]

final abstract class MultiplyG[+Factors >: `-H`[ArgsG] <: `+H`[ArgsG]] extends ExprG[NatD[TopN]]

final abstract class CompareG[
  +Left >: `-H`[ExprG[NatD[TopN]]] <: `+H`[ExprG[NatD[TopN]]],
  +Right >: `-H`[ExprG[NatD[TopN]]] <: `+H`[ExprG[NatD[TopN]]],
] extends ExprG[BoolD]

final abstract class ConjoinG[+Conjuncts >: `-H`[ArgsG] <: `+H`[ArgsG]] extends ExprG[BoolD]

final abstract class DisjoinG[+Disjuncts >: `-H`[ArgsG] <: `+H`[ArgsG]] extends ExprG[BoolD]

//endregion - Other Expression Polars

//region ==== Other Extractor Polars ====

final abstract class InspectG[
  -PayloadType >: BotD <: TopD,
  +Signature >: `-H`[XctrG[IdentifierD[TopK]]] <: `+H`[XctrG[IdentifierD[TopK]]],
  +Payload >: `-H`[XctrG[PayloadType]] <: `+H`[XctrG[PayloadType]],
] extends XctrG[CertificateD[TopK, PayloadType]]

//endregion - Other Extractor Polars

//region ==== Synonyms ====

type IdentifierPolarG = PolarG[`D:`[IdentifierD[TopK], OffBotD] | `D:`[OffTopD, IdentifierD[BotK]]]
type BoolPolarG = PolarG[`D:`[BoolD, OffBotD] | `D:`[OffTopD, `-BoolD`]]
type NatPolarG = PolarG[`D:`[NatD[TopN], OffBotD] | `D:`[OffTopD, NatD[BotN]]]

//endregion - Synonyms
