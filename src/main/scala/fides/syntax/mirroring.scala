package fides.syntax

// -------------------------------------------------------------------------------------------------
// This file contains boilerplate for augmenting/closing hierarchies under mirroring.
// -------------------------------------------------------------------------------------------------

import scala.annotation.unchecked.uncheckedVariance

/**
  * G closure
  */
type TopH = `+H`[OffTopG]
sealed trait `+H`[+G <: OffTopG]
final abstract class `-H`[-G <: OffTopG] extends `+H`[Nothing]
type BotH = `-H`[OffTopG]

/**
  * Ideally, `-H` would be defined thusly. But because this doesn't work, this type is only here for documentation.
  */
private final abstract class `ideal -H`[-G <: OffTopG] extends `+H`[G @uncheckedVariance]

// todo HUnionR & Co (Naive type union, `|`, doesn't do the right thing because we cannot use `ideal -H`)

/**
  * Correct subtyping relation for H
  */
sealed trait HSubtypeR[H1 >: BotH <: TopH, H2 >: BotH <: TopH]
object HSubtypeR:
  given [G1 <: G2, G2 <: TopG] => HSubtypeR[`+H`[G1], `+H`[G2]]
  given [G1 <: G2, G2 <: TopG] => HSubtypeR[`-H`[G2], `-H`[G1]]
  given `1`: [G1 <: G2, G2 <: TopG] => HSubtypeR[`-H`[G1], `+H`[G2]]()
  given `2`: [G1 <: G2, G2 <: TopG] => HSubtypeR[`-H`[G2], `+H`[G1]]()
end HSubtypeR

/**
  * Inversion relation for H
  */
sealed trait HInvR[`+H` >: BotH <: TopH, `-H` >: BotH <: TopH]
sealed trait HInvLR:
  given [G <: TopG] => HInvR[`+H`[G], `-H`[G]]
object HInvR extends HInvLR:
  given `0`: [G <: OffTopG] => HInvR[`-H`[G], `+H`[G]]()
  given `1`: HInvR[`+H`[EmptyArgsG], `+H`[EmptyArgsG]]()
  given `2`: [
    HeadInv >: BotH <: TopH, TailInv >: `-H`[ArgsG] <: `+H`[ArgsG],
    Head >: BotH <: TopH, Tail >: `-H`[ArgsG] <: `+H`[ArgsG],
  ] => (HInvR[Head, HeadInv], HInvR[Tail, TailInv]) => HInvR[
    `+H`[NonEmptyArgsG[Head, Tail]],
    `+H`[NonEmptyArgsG[HeadInv, TailInv]],
  ]()
end HInvR

/**
  * Inversion relation for D
  */
sealed trait DInvR[`+D` >: BotD <: TopD, `-D` >: BotD <: TopD]
object DInvR:
  given DInvR[EmptyRecordD,  EmptyRecordD]
  given [
    KeyInv >: BotK <: TopK, ValueInv >: BotD <: TopD, TailInv >: `-RecordD` <: RecordD,
    Key >: BotK <: TopK, Value >: BotD <: TopD, Tail >: `-RecordD` <: RecordD,
  ] => (KInvR[Key, KeyInv], DInvR[Value, ValueInv], DInvR[Tail, TailInv]) => DInvR[
    NonEmptyRecordD[Key, Value, Tail],
    NonEmptyRecordD[KeyInv, ValueInv, TailInv],
  ]
end DInvR

/**
  * Inversion relation for K
  */
sealed trait KInvR[K >: BotK <: TopK, `-K` >: BotK <: TopK]
sealed trait KInvLR:
  given [K >: BotK <: TopK] => KInvR[K, K]
object KInvR extends KInvLR:
  given KInvR[BotK, TopK]
  given KInvR[TopK, BotK]
end KInvR
