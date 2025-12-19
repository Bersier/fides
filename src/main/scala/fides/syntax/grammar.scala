package fides.syntax

sealed trait OffTopG private[syntax]()
final abstract class OffBotG extends BotG

sealed trait TopG extends OffTopG
sealed trait BotG extends `-XpolarG`, `-ArgsG`

sealed trait ArgsG[+Bound >: BotG <: TopG] extends TopG
sealed trait EmptyArgsG extends ArgsG[BotG]
sealed trait NonEmptyArgsG[
  +Bound >: BotG <: TopG,
  +Head >: BotG <: Bound, +Tail >: `-ArgsG` <: ArgsG[Bound],
] extends ArgsG[Bound]
sealed trait `-ArgsG` extends EmptyArgsG, NonEmptyArgsG[BotG, BotG, `-ArgsG`]

sealed trait XpolarG extends TopG
sealed trait `-XpolarG` extends `-PolarG`

sealed trait PolarG[+D >: `BotD:` <: `TopD:`] extends XpolarG
sealed trait `-PolarG` extends
  `-RecordG`, VariantG[`BotD:`, BotK, `-PolarG`], `-MultisetG`, QuoteG[`BotD:`, BotK, BotM],
  CertificateG[`BotD:`, BotK, `-PolarG`], IdentifierG[`BotD:`, `BotK:`],
  AddressG[`BotD:`, `-PolarG`, `TopD:`]

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

sealed trait QuoteG[
  +SelfD >: `BotD:` <: `TopD:`,
  +K >: BotK <: TopK, +Body >: BotM <: TopM,
] extends PolarG[SelfD]

sealed trait CertificateG[
  +SelfD >: `BotD:` <: `TopD:`,
  +K >: BotK <: TopK, +Value  >: `-PolarG` <: PolarG[`TopD:`],
] extends PolarG[SelfD]

sealed trait IdentifierG[
  +SelfD >: `BotD:` <: `TopD:`,
  +K >: `BotK:` <: `TopK:`,
] extends PolarG[SelfD]

sealed trait AddressG[
  +SelfD >: `BotD:` <: `TopD:`,
  +Name >: `-PolarG` <: PolarG[`D:`[IdentifierD[TopK], OffBotD] | `D:`[OffTopD, IdentifierD[BotK]]],
  -D >: `BotD:` <: `TopD:`,
] extends PolarG[SelfD]

sealed trait BagG[
  +SelfD >: `BotD:` <: `TopD:`,
  -`D-` >: `BotD:` <: `TopD:`, +`D+` >: `BotD:` <: `TopD:`,
  +Elements >: `-ArgsG` <: ArgsG[PolarG[`D+`]],
] extends PolarG[SelfD]

//sealed trait OrderG[+] extends PolarG[`D0`]
//sealed trait KillG extends OrderG
//sealed trait PauseG extends OrderG
//sealed trait StartG extends OrderG
//sealed trait `-OrderG` extends KillG, PauseG, StartG
