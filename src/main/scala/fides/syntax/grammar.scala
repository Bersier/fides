package fides.syntax

sealed trait OffTopG private[syntax]()
final abstract class OffBotG extends BotG

sealed trait TopG extends OffTopG
sealed trait BotG extends `-XpolarG`, `-ArgsG`

sealed trait ArgsG[-LowerBound >: BotG <: TopG, +UpperBound >: BotG <: TopG] extends TopG
sealed trait EmptyArgsG extends ArgsG[TopG, BotG]
sealed trait NonEmptyArgsG[
  -LowerBound >: BotG <: TopG, +UpperBound >: BotG <: TopG,
  +Head >: LowerBound <: UpperBound, +Tail >: `-ArgsG` <: ArgsG[LowerBound, UpperBound],
] extends ArgsG[LowerBound, UpperBound]
sealed trait `-ArgsG` extends EmptyArgsG, NonEmptyArgsG[TopG, BotG, ?, `-ArgsG`]

sealed trait XpolarG extends TopG
sealed trait `-XpolarG` extends `-PolarG`[`BotD:`]

sealed trait PolarG[+D >: `BotD:` <: `TopD:`] extends XpolarG
sealed trait `-PolarG`[+D >: `BotD:` <: `TopD:`] extends
  `-RecordG`, VariantG[`BotD:`, BotK, `-PolarG`[`BotD:`]], `-MultisetG`, QuoteG[`BotD:`, BotK, BotM],
  CertificateG[`BotD:`, BotK, `-PolarG`[`BotD:`]], IdentifierG[`BotD:`, `BotK:`],
  AddressG[`BotD:`, `-PolarG`[`BotD:`], `TopD:`]

// ==== Constructors/Destructors ====

sealed trait RecordG[+SelfD >: `BotD:` <: `TopD:`] extends PolarG[SelfD]
sealed trait EmptyRecordG extends RecordG[`D0`[EmptyRecordD]]
sealed trait NonEmptyRecordG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Key >: BotK <: TopK, +Value >: `-PolarG`[`BotD:`] <: PolarG[`TopD:`], +Tail >: `-RecordG` <: RecordG[`TopD:`],
] extends RecordG[SelfD]
sealed trait `-RecordG` extends EmptyRecordG, NonEmptyRecordG[`BotD:`, BotK, `-PolarG`[`BotD:`], `-RecordG`]

sealed trait VariantG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Key >: BotK <: TopK, +Value >: `-PolarG`[`BotD:`] <: PolarG[`TopD:`],
] extends PolarG[SelfD]

sealed trait MultisetG[+SelfD >: `BotD:` <: `TopD:`] extends PolarG[SelfD]
sealed trait EmptyMultisetG extends MultisetG[`D0`[EmptyMultisetD]]
sealed trait NonEmptyMultisetG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Head >: `-PolarG`[`BotD:`] <: PolarG[`TopD:`], +Tail >: `-MultisetG` <: MultisetG[`TopD:`],
] extends MultisetG[SelfD]
sealed trait `-MultisetG` extends EmptyMultisetG, NonEmptyMultisetG[`BotD:`, `-PolarG`[`BotD:`], `-MultisetG`]

sealed trait QuoteG[
  +SelfD >: `BotD:` <: `TopD:`,
  +K >: BotK <: TopK, +Body >: BotM <: TopM,
] extends PolarG[SelfD]

sealed trait CertificateG[
  +SelfD >: `BotD:` <: `TopD:`,
  +K >: BotK <: TopK, +Value  >: `-PolarG`[`BotD:`] <: PolarG[`TopD:`],
] extends PolarG[SelfD]

sealed trait IdentifierG[
  +SelfD >: `BotD:` <: `TopD:`,
  +K >: `BotK:` <: `TopK:`,
] extends PolarG[SelfD]

sealed trait AddressG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Name >: `-PolarG`[`BotD:`] <: PolarG[`D:`[IdentifierD[TopK], OffBotD] | `D:`[OffTopD, IdentifierD[BotK]]],
  -D >: `BotD:` <: `TopD:`,
] extends PolarG[SelfD]

sealed trait BagG[
  +SelfD >: `BotD:` <: `TopD:`,
  -`D-` >: `BotD:` <: `TopD:`, +`D+` >: `BotD:` <: `TopD:`,
  +Elements >: `-ArgsG` <: ArgsG[`-PolarG`[`D-`], PolarG[`D+`]],
] extends PolarG[SelfD]

//sealed trait OrderG[+] extends PolarG[`D0`]
//sealed trait KillG extends OrderG
//sealed trait PauseG extends OrderG
//sealed trait StartG extends OrderG
//sealed trait `-OrderG` extends KillG, PauseG, StartG
