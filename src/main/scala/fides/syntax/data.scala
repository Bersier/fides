package fides.syntax

// -------------------------------------------------------------------------------------------------
// This file contains the constructors to build Fides data types.
// -------------------------------------------------------------------------------------------------

/**
  * A type greater than [[TopD]]
  *
  * Indicates an illegal data type in covariant position (and it's unreachable in contravariant position).
  */
sealed trait OffTopD private[syntax]()

/**
  * Anything; Fides' version of [[Any]]
  *
  * Upper bound for all data types
  */
sealed trait TopD extends OffTopD

/**
  * Labeled product type
  *
  * The type of a record is a set of key-(value-type) pairs. But we cannot represent unordered types in Scala,
  * so we instead use a list of key-(value-type) pairs. It is assumed that it is sorted by key and without duplicate,
  * so it's a canonical representation of the set of pairs.
  */
sealed trait RecordD extends TopD
final abstract class EmptyRecordD extends RecordD
final abstract class NonEmptyRecordD[
  +Key >: BotK <: TopK, +Value >: ND[TopD] <: PD[TopD], +Tail >: ND[RecordD] <: PD[RecordD],
] extends RecordD

/**
  * Labeled value
  */
final abstract class EntryD[+Key >: BotK <: TopK, +Value >: ND[TopD] <: PD[TopD]] extends TopD

/**
  * Labeled sum type
  *
  * The type of a variant is a set of key-(value-type) pairs. But we cannot represent unordered types in Scala,
  * so we instead use a list of key-(value-type) pairs. It is assumed that it is sorted by key and without duplicate,
  * so it's a canonical representation of the set of pairs.
  */
sealed trait VariantD extends TopD
final abstract class EmptyVariantD extends VariantD
final abstract class NonEmptyVariantD[
  +Key >: BotK <: TopK, +Value >: ND[TopD] <: PD[TopD], +Tail >: ND[VariantD] <: PD[VariantD],
] extends VariantD

/**
  * Heterogeneous unordered collection
  *
  * The type of a (heterogeneous) multiset is a multiset of data types.
  * But we cannot represent unordered types in Scala, so we instead use a list of data types.
  * It is assumed that it is sorted by data type, so it's a canonical representation of the multiset.
  */
sealed trait BagD[+Element >: ND[TopD] <: PD[TopD]] extends TopD
final abstract class EmptyBagD extends BagD[ND[TopD]]
final abstract class NonEmptyBagD[
  +TailBound >: ND[TopD] <: PD[TopD],
  +Head >: ND[TopD] <: PD[TopD], +Tail >: ND[BagD[TailBound]] <: PD[BagD[TailBound]],
] extends BagD[Head | TailBound]

/**
  * Quote that hasn't been syntactically checked
  */
final abstract class PreQuoteD[+Body >: BotM <: TopM] extends TopD

/**
  * Code as value
  */
final abstract class QuoteD[+Body >: BotM <: TopM] extends TopD

/**
  * "Compiled" quote
  */
final abstract class BehaviorD[+Behavior >: NG[XpolarG] <: PG[XpolarG]] extends TopD

/**
  * Signed value
  */
final abstract class CertificateD[+K >: BotK <: TopK, +Payload >: ND[TopD] <: PD[TopD]] extends TopD

/**
  * Name as value
  */
final abstract class IdentifierD[+K >: BotK <: TopK] extends TopD

/**
  * Channel address
  */
final abstract class AddressD[+K >: BotK <: TopK, -Data >: ND[TopD] <: PD[TopD]] extends TopD

/**
  * Natural number
  */
final abstract class NatD[+N >: BotN <: TopN] extends TopD

/**
  * Process control order
  */
sealed trait OrderD extends TopD
final abstract class KillD extends OrderD
final abstract class PauseD extends OrderD
final abstract class StartD extends OrderD

/**
  * Boolean
  */
sealed trait BoolD extends TopD
final abstract class FalseD extends BoolD
final abstract class TrueD extends BoolD

/**
  * Unit
  */
final abstract class PulseD extends TopD
