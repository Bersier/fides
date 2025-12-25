package fides.syntax

import scala.annotation.unchecked.uncheckedVariance

type TopH = `+H`[OffTopG]
sealed trait `+H`[+G <: OffTopG]
final abstract class `-H`[-G <: OffTopG] extends `+H`[G @uncheckedVariance] // todo...
type BotH = `-H`[OffTopG]

sealed trait HInvR[-H >: BotH <: TopH, `-H` >: BotH <: TopH]
sealed trait HInvLR:
  given [G <: TopG] => HInvR[`+H`[G] | BotH, `-H`[G]]
object HInvR:
  given `0`: [G <: OffTopG] => HInvR[`-H`[G], `+H`[G] | BotH]()
  given `1`: HInvR[`+H`[EmptyArgsG] | BotH, `+H`[EmptyArgsG] | BotH]()
  given `2`: [
    HeadInv >: BotH <: TopH, TailInv >: `-H`[ArgsG] <: `+H`[ArgsG],
    Head >: BotH <: TopH, Tail >: `-H`[ArgsG] <: `+H`[ArgsG],
  ] => (HInvR[Head, HeadInv], HInvR[Tail, TailInv]) => HInvR[
    `+H`[NonEmptyArgsG[Head, Tail]] | BotH,
    `+H`[NonEmptyArgsG[HeadInv, TailInv]] | BotH,
  ]()
end HInvR

type DInv[D >: BotD <: TopD] <: TopD = D match
  case EmptyRecordD => EmptyRecordD
  case NonEmptyRecordD[k, v, t] => NonEmptyRecordD[KInv[k], DInv[v], DInv[t]]
  // todo...
