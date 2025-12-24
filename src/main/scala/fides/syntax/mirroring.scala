package fides.syntax

import scala.annotation.unchecked.uncheckedVariance

sealed trait TopH
sealed trait `+H`[+G <: OffTopG] extends TopH
final abstract class `-H`[-G <: OffTopG] extends `+H`[G @uncheckedVariance]

type `-`[H <: TopH] <: TopH = H match
  case `-H`[g] => `+H`[g]
  case `+H`[EmptyArgsG] => `+H`[EmptyArgsG]
  case `+H`[NonEmptyArgsG[h, t]] => `+H`[NonEmptyArgsG[`-`[h], `-`[t]]]
  // todo add more leaf cases
  case `+H`[g] => `-H`[g]
