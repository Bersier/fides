package fides.syntax

// -------------------------------------------------------------------------------------------------
// This file contains boilerplate for augmenting/closing the D hierarchy under mirroring.
// -------------------------------------------------------------------------------------------------

import scala.annotation.unchecked.uncheckedVariance

/**
  * D closure
  */
sealed trait PD[+D <: OffTopD]
final abstract class ND[-D <: OffTopD] extends PD[Nothing]

/**
  * Ideally, [[ND]] would be defined thusly. But because this doesn't work, this type is only here for documentation.
  */
private final abstract class `ideal ND`[-D <: OffTopD] extends PD[D @uncheckedVariance]

/**
  * Correct subtyping relation for D
  */
sealed trait DSubtypeR[D1 >: ND[OffTopD] <: PD[OffTopD], D2 >: ND[OffTopD] <: PD[OffTopD]]
object DSubtypeR:
  given [D1 <: D2, D2 <: TopD] => DSubtypeR[PD[D1], PD[D2]]
  given [D1 <: D2, D2 <: TopD] => DSubtypeR[ND[D2], ND[D1]]
  given `1`: [D1 <: D2, D2 <: TopD] => DSubtypeR[ND[D1], PD[D2]]()
  given `2`: [D1 <: D2, D2 <: TopD] => DSubtypeR[ND[D2], PD[D1]]()
end DSubtypeR

/**
  * Inversion relation for D
  */
sealed trait DInvR[`+D` >: ND[TopD] <: PD[TopD], `-D` >: ND[TopD] <: PD[TopD]]
object DInvR:
  given DInvR[PD[EmptyRecordD],  PD[EmptyRecordD]]
  given [
    KeyInv >: BotK <: TopK, ValueInv >: ND[TopD] <: PD[TopD], TailInv >: ND[RecordD] <: PD[RecordD],
    Key >: BotK <: TopK, Value >: ND[TopD] <: PD[TopD], Tail >: ND[RecordD] <: PD[RecordD],
  ] => (KInvR[Key, KeyInv], DInvR[Value, ValueInv], DInvR[Tail, TailInv]) => DInvR[
    PD[NonEmptyRecordD[Key, Value, Tail]],
    PD[NonEmptyRecordD[KeyInv, ValueInv, TailInv]],
  ]
end DInvR
