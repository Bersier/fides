package fides.syntax

// -------------------------------------------------------------------------------------------------
// This file contains boilerplate for augmenting/closing hierarchies under mirroring.
// -------------------------------------------------------------------------------------------------

import scala.annotation.unchecked.uncheckedVariance

/**
  * D closure
  */
sealed trait `+E`[+D <: OffTopD]
final abstract class `-E`[-D <: OffTopD] extends `+E`[Nothing]

/**
  * Ideally, `-E` would be defined thusly. But because this doesn't work, this type is only here for documentation.
  */
private final abstract class `ideal -E`[-D <: OffTopD] extends `+E`[D @uncheckedVariance]

/**
  * G closure
  */
sealed trait `+H`[+G <: OffTopG]
final abstract class `-H`[-G <: OffTopG] extends `+H`[Nothing]
/**
  * Ideally, `-H` would be defined thusly. But because this doesn't work, this type is only here for documentation.
  */
private final abstract class `ideal -H`[-G <: OffTopG] extends `+H`[G @uncheckedVariance]

// todo HUnionR & Co (Naive type union, `|`, doesn't do the right thing because we cannot use `ideal -H`)

/**
  * Correct subtyping relation for E
  */
sealed trait ESubtypeR[E1 >: `-E`[OffTopD] <: `+E`[OffTopD], E2 >: `-E`[OffTopD] <: `+E`[OffTopD]]
object ESubtypeR:
  given [D1 <: D2, D2 <: TopD] => ESubtypeR[`+E`[D1], `+E`[D2]]
  given [D1 <: D2, D2 <: TopD] => ESubtypeR[`-E`[D2], `-E`[D1]]
  given `1`: [D1 <: D2, D2 <: TopD] => ESubtypeR[`-E`[D1], `+E`[D2]]()
  given `2`: [D1 <: D2, D2 <: TopD] => ESubtypeR[`-E`[D2], `+E`[D1]]()
end ESubtypeR

/**
  * Correct subtyping relation for H
  */
sealed trait HSubtypeR[H1 >: `-H`[OffTopG] <: `+H`[OffTopG], H2 >: `-H`[OffTopG] <: `+H`[OffTopG]]
object HSubtypeR:
  given [G1 <: G2, G2 <: TopG] => HSubtypeR[`+H`[G1], `+H`[G2]]
  given [G1 <: G2, G2 <: TopG] => HSubtypeR[`-H`[G2], `-H`[G1]]
  given `1`: [G1 <: G2, G2 <: TopG] => HSubtypeR[`-H`[G1], `+H`[G2]]()
  given `2`: [G1 <: G2, G2 <: TopG] => HSubtypeR[`-H`[G2], `+H`[G1]]()
end HSubtypeR

/**
  * Inversion relation for H
  */
sealed trait HInvR[`+ H` >: `-H`[OffTopG] <: `+H`[OffTopG], `- H` >: `-H`[OffTopG] <: `+H`[OffTopG]]
sealed trait HInvLR:
  given [G <: TopG] => HInvR[`+H`[G], `-H`[G]]
object HInvR extends HInvLR:
  given `0`: [G <: OffTopG] => HInvR[`-H`[G], `+H`[G]]()
  given `1`: HInvR[`+H`[EmptyArgsG], `+H`[EmptyArgsG]]()
  given `2`: [
    HeadInv >: `-H`[TopG] <: `+H`[TopG], TailInv >: `-H`[ArgsG] <: `+H`[ArgsG],
    Head >: `-H`[TopG] <: `+H`[TopG], Tail >: `-H`[ArgsG] <: `+H`[ArgsG],
  ] => (HInvR[Head, HeadInv], HInvR[Tail, TailInv]) => HInvR[
    `+H`[NonEmptyArgsG[Head, Tail]],
    `+H`[NonEmptyArgsG[HeadInv, TailInv]],
  ]()
end HInvR

/**
  * Inversion relation for E
  */
sealed trait EInvR[`+ E` >: `-E`[TopD] <: `+E`[TopD], `- E` >: `-E`[TopD] <: `+E`[TopD]]
object EInvR:
  given EInvR[`+E`[EmptyRecordD],  `+E`[EmptyRecordD]]
  given [
    KeyInv >: BotK <: TopK, ValueInv >: `-E`[TopD] <: `+E`[TopD], TailInv >: `-E`[RecordD] <: `+E`[RecordD],
    Key >: BotK <: TopK, Value >: `-E`[TopD] <: `+E`[TopD], Tail >: `-E`[RecordD] <: `+E`[RecordD],
  ] => (KInvR[Key, KeyInv], EInvR[Value, ValueInv], EInvR[Tail, TailInv]) => EInvR[
    `+E`[NonEmptyRecordD[Key, Value, Tail]],
    `+E`[NonEmptyRecordD[KeyInv, ValueInv, TailInv]],
  ]
end EInvR

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
