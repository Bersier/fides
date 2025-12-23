package fides.syntax

// -------------------------------------------------------------------------------------------------
// This file contains the constructors to build a full characterization of any piece of Fides code,
// except for top-level escapes.
//
// Additional grammar rules are in `grules.scala`.
// -------------------------------------------------------------------------------------------------

sealed trait OffTopG private[syntax]()
final abstract class OffBotG extends BotG

sealed trait TopG extends OffTopG
sealed trait BotG extends `-XpolarG`, `-ArgsG`

/**
  * An unordered list of Gs
  *
  * Because we cannot represent unordered types in Scala, we instead use a list of G types.
  * It is assumed that it is sorted by G type, so it's a canonical representation of the G multiset.
  */
sealed trait ArgsG extends TopG
sealed trait EmptyArgsG extends ArgsG
sealed trait NonEmptyArgsG[+Head >: BotG <: TopG, +Tail >: `-ArgsG` <: ArgsG] extends ArgsG
sealed trait `-ArgsG` extends EmptyArgsG, NonEmptyArgsG[BotG, `-ArgsG`]

//region ==== Locations ====

sealed trait LocG[+K <: TopK, +Datatype >: `BotD:` <: `TopD:`] extends TopG

//endregion - Locations

//region ==== Xpolar Categories ====

sealed trait XpolarG extends TopG
sealed trait `-XpolarG` extends `-ApolarG`, `-PolarG`, BipolarG[`BotD::`, `BotD::`]

sealed trait ApolarG extends XpolarG
sealed trait `-ApolarG` extends ConcurrentG[`-ArgsG`], RepeatedG[`-ApolarG`], SendG[`-PolarG`, `-PolarG`]

sealed trait PolarG[+D >: `BotD:` <: `TopD:`] extends XpolarG
type ExprG[+D >: BotD <: TopD] = PolarG[`D+`[D]]
type XctrG[-D >: BotD <: TopD] = PolarG[`D-`[D]]
sealed trait `-PolarG` extends // todo incorrect mirroring
  AddG[`-ArgsG`],
  AddressG[`BotD:`, `-PolarG`, `TopD:`],
  BagG[`BotD:`, `-ArgsG`],
  CertificateHG[`BotD:`, `BotK:`, `-PolarG`],
  CollectHG[`BotD:`, `BotK:`, `BotD:`, `-PolarG`],
  CompareG[`-PolarG`, `-PolarG`],
  ConjoinG[`-ArgsG`],
  DisjoinG[`-ArgsG`],
  IdentifierHG[`BotD:`, `BotK:`],
  InspectG[BotD, `-PolarG`, `-PolarG`],
  MergeBagsG[BotD, `-PolarG`, `-PolarG`],
  MultiplyG[`-ArgsG`],
  MultisetG[`BotD:`, `-ArgsG`],
  NatG[`BotD:`, `BotN:`],
  NegateG[`BotD:`, `-PolarG`],
  PulseG,
  QuoteHG[`BotD:`, `BotK:`, BotM],
  VariantHG[`BotD:`, `BotK:`, `-PolarG`],
  `-RecordG`

sealed trait BipolarG[+I >: `BotD::` <: `TopD::`, +O >: `BotD::` <: `TopD::`] extends XpolarG

//endregion - Xpolar Categories

//region ==== Apolars ====

sealed trait ConcurrentG[+Processes >: `-ArgsG` <: ArgsG] extends ApolarG

sealed trait RepeatedG[+Process >: `-ApolarG` <: ApolarG] extends ApolarG

sealed trait SendG[
  +Contents >: `-PolarG` <: ExprG[TopD], +Recipient >: `-PolarG` <: ExprG[AddressD[TopK, BotD]],
] extends ApolarG

//endregion - Apolars

//region ==== Constructor/Destructor Polars ====

sealed trait RecordG

sealed trait EmptyRecordG extends RecordG, PolarG[`D0`[EmptyRecordD]]

/**
  * Helper type. It helps with being able to take the lower bound over [[NonEmptyRecordG]],
  * even though the latter is invariant in [[K]].
  */
private sealed trait NonEmptyRecordHG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Key >: `BotK:` <: `TopK:`, +Value >: `-PolarG` <: PolarG[`TopD:`], +Tail >: `-RecordG` <: RecordG,
] extends RecordG, PolarG[SelfD]

type NonEmptyRecordG[
  +SelfD >: `BotD:` <: `TopD:`,
  Key >: BotK <: TopK, +Value >: `-PolarG` <: PolarG[`TopD:`], +Tail >: `-RecordG` <: RecordG,
] = NonEmptyRecordHG[SelfD, `K0`[Key], Value, Tail]

sealed trait `-RecordG` extends EmptyRecordG, NonEmptyRecordHG[`BotD:`, `BotK:`, `-PolarG`, `-RecordG`]

/**
  * Helper type. It helps with being able to take the lower bound over [[VariantG]],
  * even though the latter is invariant in [[K]].
  */
private sealed trait VariantHG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Key >: `BotK:` <: `TopK:`, +Value >: `-PolarG` <: PolarG[`TopD:`],
] extends PolarG[SelfD]

type VariantG[
  +SelfD >: `BotD:` <: `TopD:`,
  Key >: BotK <: TopK, +Value >: `-PolarG` <: PolarG[`TopD:`],
] = VariantHG[SelfD, `K0`[Key], Value]

sealed trait MultisetG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Elements >: `-ArgsG` <: ArgsG,
] extends PolarG[SelfD]

sealed trait BagG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Elements >: `-ArgsG` <: ArgsG,
] extends PolarG[SelfD]

/**
  * Helper type. It helps with being able to take the lower bound over [[QuoteG]],
  * even though the latter is invariant in [[K]].
  */
private sealed trait QuoteHG[
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
private sealed trait CertificateHG[
  +SelfD >: `BotD:` <: `TopD:`,
  +K >: `BotK:` <: `TopK:`, +Payload  >: `-PolarG` <: PolarG[`TopD:`],
] extends PolarG[SelfD]

type CertificateG[
  +SelfD >: `BotD:` <: `TopD:`,
  K >: BotK <: TopK, +Payload  >: `-PolarG` <: PolarG[`TopD:`],
] = CertificateHG[SelfD, `K0`[K], Payload]

/**
  * Helper type. It helps with being able to take the lower bound over [[IdentifierG]],
  * even though the latter is invariant in [[K]].
  */
private sealed trait IdentifierHG[
  +SelfD >: `BotD:` <: `TopD:`,
  +K >: `BotK:` <: `TopK:`,
] extends PolarG[SelfD]

type IdentifierG[K >: BotK <: TopK] = IdentifierHG[`D0`[IdentifierD[K]], `K0`[K]]

sealed trait AddressG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Name >: `-PolarG` <: PolarG[`D:`[IdentifierD[TopK], OffBotD] | `D:`[OffTopD, IdentifierD[BotK]]],
  -Datatype >: `BotD:` <: `TopD:`,
] extends PolarG[SelfD]

sealed trait NatG[
  +SelfD >: `BotD:` <: `TopD:`,
  +N >: `BotN:` <: `TopN:`,
] extends PolarG[SelfD]

sealed trait KillG extends PolarG[`D0`[KillD]]
sealed trait PauseG extends PolarG[`D0`[PauseD]]
sealed trait StartG extends PolarG[`D0`[StartD]]

sealed trait FalseG extends PolarG[`D0`[FalseD]]
sealed trait TrueG extends PolarG[`D0`[TrueD]]

sealed trait PulseG extends PolarG[`D0`[PulseD]]

//endregion - Constructor/Destructor Polars

//region ==== Other Reversible Polars ====

/**
  * Helper type. It helps with being able to take the lower bound over [[CollectG]],
  * even though the latter is invariant in [[K]].
  */
private sealed trait CollectHG[
  +SelfD >: `BotD:` <: `TopD:`,
  +K >: `BotK:` <: `TopK:`, +ElementType >: `BotD:` <: `TopD:`,
  +Size >: `-PolarG` <: PolarG[`D:`[NatD[TopN], OffBotD] | `D:`[OffTopD, NatD[BotN]]],
] extends PolarG[SelfD]

type CollectG[
  +SelfD >: `BotD:` <: `TopD:`,
  K >: BotK <: TopK, +ElementType >: `BotD:` <: `TopD:`,
  +Size >: `-PolarG` <: PolarG[`D:`[NatD[TopN], OffBotD] | `D:`[OffTopD, NatD[BotN]]],
] = CollectHG[SelfD, `K0`[K], ElementType, Size]

sealed trait NegateG[
  +SelfD >: `BotD:` <: `TopD:`,
  +B >: `-PolarG` <: PolarG[`D:`[BoolD, OffBotD] | `D:`[OffTopD, `-BoolD`]],
] extends PolarG[SelfD]

//endregion - Other Reversible Polars

//region ==== Other Expression Polars ====

sealed trait MergeBagsG[
  +ElementType >: BotD <: TopD,
  +Bag1 >: `-PolarG` <: ExprG[BagD[ElementType]],
  +Bag2 >: `-PolarG` <: ExprG[BagD[ElementType]],
] extends ExprG[BagD[ElementType]]

sealed trait AddG[+Terms >: `-ArgsG` <: ArgsG] extends ExprG[NatD[TopN]]

sealed trait MultiplyG[+Factors >: `-ArgsG` <: ArgsG] extends ExprG[NatD[TopN]]

sealed trait CompareG[
  +Left >: `-PolarG` <: ExprG[NatD[TopN]], +Right >: `-PolarG` <: ExprG[NatD[TopN]],
] extends ExprG[BoolD]

sealed trait ConjoinG[+Conjuncts >: `-ArgsG` <: ArgsG] extends ExprG[BoolD]

sealed trait DisjoinG[+Disjuncts >: `-ArgsG` <: ArgsG] extends ExprG[BoolD]

//endregion - Other Expression Polars

//region ==== Other Extractor Polars ====

sealed trait InspectG[
  -PayloadType >: BotD <: TopD,
  +Signature >: `-PolarG` <: XctrG[IdentifierD[TopK]], +Payload >: `-PolarG` <: XctrG[PayloadType],
] extends XctrG[CertificateD[TopK, PayloadType]]

//endregion - Other Extractor Polars
