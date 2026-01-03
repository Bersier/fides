package fides.syntax

// -------------------------------------------------------------------------------------------------
// This file contains boilerplate for augmenting/closing the G hierarchy under mirroring.
// -------------------------------------------------------------------------------------------------

import scala.annotation.unchecked.uncheckedVariance

/**
  * G closure
  */
sealed trait PG[+G <: OffTopG]
final abstract class NG[-G <: OffTopG] extends PG[Nothing]

/**
  * Ideally, [[NG]] would be defined thusly. But because this doesn't work, this type is only here for documentation.
  */
private final abstract class `ideal NG`[-G <: OffTopG] extends PG[G @uncheckedVariance]

// todo GUnionR & Co (Naive type union, `|`, doesn't do the right thing because we cannot use `ideal NG`)

/**
  * Correct subtyping relation for G
  */
sealed trait GSubtypeR[G1 >: NG[OffTopG] <: PG[OffTopG], G2 >: NG[OffTopG] <: PG[OffTopG]]
object GSubtypeR:
  given [G1 <: G2, G2 <: TopG] => GSubtypeR[PG[G1], PG[G2]]
  given [G1 <: G2, G2 <: TopG] => GSubtypeR[NG[G2], NG[G1]]
  given `1`: [G1 <: G2, G2 <: TopG] => GSubtypeR[NG[G1], PG[G2]]()
  given `2`: [G1 <: G2, G2 <: TopG] => GSubtypeR[NG[G2], PG[G1]]()
end GSubtypeR

/**
  * Inversion relation for G
  */
sealed trait GInvR[`+G` >: NG[OffTopG] <: PG[OffTopG], `-G` >: NG[OffTopG] <: PG[OffTopG]]
sealed trait GInvLR:
  given [G <: TopG] => GInvR[PG[G], NG[G]]
object GInvR extends GInvLR:
  given `0`: [G <: OffTopG] => GInvR[NG[G], PG[G]]()
  given `1`: GInvR[PG[EmptyArgsG], PG[EmptyArgsG]]()
  given `2`: [
    HeadInv >: NG[TopG] <: PG[TopG], TailInv >: NG[ArgsG] <: PG[ArgsG],
    Head >: NG[TopG] <: PG[TopG], Tail >: NG[ArgsG] <: PG[ArgsG],
  ] => (GInvR[Head, HeadInv], GInvR[Tail, TailInv]) => GInvR[
    PG[NonEmptyArgsG[Head, Tail]],
    PG[NonEmptyArgsG[HeadInv, TailInv]],
  ]()
end GInvR
