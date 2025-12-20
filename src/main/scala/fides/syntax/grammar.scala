package fides.syntax

sealed trait OffTopG private[syntax]()
final abstract class OffBotG extends BotG

sealed trait TopG extends OffTopG
sealed trait BotG extends `-XpolarG`, `-ArgsG`

sealed trait ArgsG extends TopG
sealed trait EmptyArgsG extends ArgsG
sealed trait NonEmptyArgsG[+Head >: BotG <: TopG, +Tail >: `-ArgsG` <: ArgsG] extends ArgsG
sealed trait `-ArgsG` extends EmptyArgsG, NonEmptyArgsG[BotG, `-ArgsG`]

sealed trait XpolarG extends TopG
sealed trait `-XpolarG` extends `-PolarG`

// ==== Locations ====

sealed trait LocG[+K <: TopK, +Datatype >: `BotD:` <: `TopD:`] extends TopG

// ==== Polars ====

sealed trait PolarG[+D >: `BotD:` <: `TopD:`] extends XpolarG
sealed trait `-PolarG` extends
  `-RecordG`, VariantG[`BotD:`, BotK, `-PolarG`], `-MultisetG`, BagG[`BotD:`, `-ArgsG`], QuoteG[`BotD:`, BotK, BotM],
  CertificateG[`BotD:`, BotK, `-PolarG`], IdentifierG[`BotD:`, `BotK:`], AddressG[`BotD:`, `-PolarG`, `TopD:`],
  `-OrderG`, `-BoolG`, PulseG, CollectG[`BotD:`, BotK, `BotD:`, `-PolarG`],
  MergeBagsG[BotD, `-PolarG`, `-PolarG`], ConjoinG[`-ArgsG`], DisjoinG[`-ArgsG`]
type ExprG[+D >: BotD <: TopD] = PolarG[`D+`[D]]
type XctrG[-D >: BotD <: TopD] = PolarG[`D-`[D]]

// ==== Constructors/Destructors ====

sealed trait RecordG[+SelfD >: `BotD:` <: `TopD:`] extends PolarG[SelfD]
sealed trait EmptyRecordG extends RecordG[`D0`[EmptyRecordD]]
sealed trait NonEmptyRecordG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Key >: BotK <: TopK, +Value >: `-PolarG` <: PolarG[`TopD:`], +Tail >: `-RecordG` <: RecordG[`TopD:`],
] extends RecordG[SelfD]
sealed trait `-RecordG` extends EmptyRecordG, NonEmptyRecordG[`BotD:`, BotK, `-PolarG`, `-RecordG`]

sealed trait VariantG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Key >: BotK <: TopK, +Value >: `-PolarG` <: PolarG[`TopD:`],
] extends PolarG[SelfD]

sealed trait MultisetG[+SelfD >: `BotD:` <: `TopD:`] extends PolarG[SelfD]
sealed trait EmptyMultisetG extends MultisetG[`D0`[EmptyMultisetD]]
sealed trait NonEmptyMultisetG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Head >: `-PolarG` <: PolarG[`TopD:`], +Tail >: `-MultisetG` <: MultisetG[`TopD:`],
] extends MultisetG[SelfD]
sealed trait `-MultisetG` extends EmptyMultisetG, NonEmptyMultisetG[`BotD:`, `-PolarG`, `-MultisetG`]

sealed trait BagG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Elements >: `-ArgsG` <: ArgsG,
] extends PolarG[SelfD]

sealed trait QuoteG[
  +SelfD >: `BotD:` <: `TopD:`,
  +K >: BotK <: TopK, +Body >: BotM <: TopM,
] extends PolarG[SelfD]

sealed trait CertificateG[
  +SelfD >: `BotD:` <: `TopD:`,
  +K >: BotK <: TopK, +Payload  >: `-PolarG` <: PolarG[`TopD:`],
] extends PolarG[SelfD]

sealed trait IdentifierG[
  +SelfD >: `BotD:` <: `TopD:`,
  +K >: `BotK:` <: `TopK:`,
] extends PolarG[SelfD]

sealed trait AddressG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Name >: `-PolarG` <: PolarG[`D:`[IdentifierD[TopK], OffBotD] | `D:`[OffTopD, IdentifierD[BotK]]],
  -Datatype >: `BotD:` <: `TopD:`,
] extends PolarG[SelfD]

sealed trait OrderG[+SelfD >: `BotD:` <: `D:`[OrderD, OffBotD] | `D:`[OffTopD, OrderD]] extends PolarG[SelfD]
sealed trait KillG extends OrderG[`D0`[KillD]]
sealed trait PauseG extends OrderG[`D0`[PauseD]]
sealed trait StartG extends OrderG[`D0`[StartD]]
sealed trait `-OrderG` extends KillG, PauseG, StartG

sealed trait BoolG[+SelfD >: `BotD:` <: `D:`[BoolD, OffBotD] | `D:`[OffTopD, BoolD]] extends PolarG[SelfD]
sealed trait FalseG extends BoolG[`D0`[FalseD]]
sealed trait TrueG extends BoolG[`D0`[TrueD]]
sealed trait `-BoolG` extends FalseG, TrueG

sealed trait PulseG extends PolarG[`D0`[PulseD]]

// ==== Other Polars ====

sealed trait CollectG[
  +SelfD >: `BotD:` <: `TopD:`,
  +K >: BotK <: TopK, +ElementType >: `BotD:` <: `TopD:`,
  +Size >: `-PolarG` <: PolarG[`D:`[NatD[TopN], OffBotD] | `D:`[OffTopD, NatD[BotN]]],
] extends PolarG[SelfD]

sealed trait MergeBagsG[
  +ElementType >: BotD <: TopD,
  +Bag1 >: `-PolarG` <: ExprG[BagD[ElementType]],
  +Bag2 >: `-PolarG` <: ExprG[BagD[ElementType]],
] extends ExprG[BagD[ElementType]]

sealed trait ConjoinG[+Conjuncts >: `-ArgsG` <: ArgsG] extends ExprG[BoolD]

sealed trait DisjoinG[+Disjuncts >: `-ArgsG` <: ArgsG] extends ExprG[BoolD]
