package fides.syntax

import scala.annotation.unchecked.uncheckedVariance

type TopH = `+H`[OffTopG]
sealed trait `+H`[+G <: OffTopG]
final abstract class `-H`[-G <: OffTopG] extends `+H`[G @uncheckedVariance]
type BotH = `-H`[OffTopG]

type HInv[H <: TopH] <: TopH = H match
  case `-H`[g] => `+H`[g]
  case `+H`[EmptyArgsG] => `+H`[EmptyArgsG]
  case `+H`[NonEmptyArgsG[h, t]] => `+H`[NonEmptyArgsG[HInv[h], HInv[t]]]
  // todo add more leaf cases here
  case `+H`[g] => `-H`[g]
