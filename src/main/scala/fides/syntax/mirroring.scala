package fides.syntax

import scala.annotation.unchecked.uncheckedVariance

type TopH = `+H`[OffTopG]
sealed trait `+H`[+G <: OffTopG]
final abstract class `-H`[-G <: OffTopG] extends `+H`[G @uncheckedVariance] // todo...
type BotH = `-H`[OffTopG]

sealed trait HInvR[-H <: TopH, `-H` <: TopH]
object HInvR:
  given `0`: [G <: OffTopG] => HInvR[`-H`[G], `+H`[G]]()
  given `1`: HInvR[`+H`[EmptyArgsG], `+H`[EmptyArgsG]]()
  given `2`: [
    Head >: BotH <: TopH, Tail >: `-H`[ArgsG] <: `+H`[ArgsG],
  ] => HInvR[
    `+H`[NonEmptyArgsG[Head, Tail]],
    `+H`[
      NonEmptyArgsG[HInv[Head] | BotH,
      (HInv[Tail] | `-H`[fides.syntax.ArgsG]) & `+H`[fides.syntax.ArgsG]],
    ],
  ]()
end HInvR

type HInv[H <: TopH] <: TopH = H match
  case `-H`[g] => `+H`[g]
  case `+H`[EmptyArgsG] => `+H`[EmptyArgsG]
  case `+H`[NonEmptyArgsG[h, t]] => `+H`[NonEmptyArgsG[HInv[h], HInv[t]]]
  // todo add more leaf cases here
  case `+H`[g] => `-H`[g]

type DInv[D >: BotD <: TopD] <: TopD = D match
  case EmptyRecordD => EmptyRecordD
  case NonEmptyRecordD[k, v, t] => NonEmptyRecordD[KInv[k], DInv[v], DInv[t]]
  // todo add more cases
