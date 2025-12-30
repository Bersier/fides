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
  * A type smaller than [[BotD]]
  *
  * Indicates an illegal data type in contravariant position (and it's unreachable in covariant position).
  */
final abstract class OffBotD extends BotD

/**
  * Anything; Fides' version of [[Any]]
  *
  * Upper bound for all data types
  */
sealed trait TopD extends OffTopD

/**
  * Nothing; Fides' version of [[Nothing]]
  *
  * Lower bound for all data types
  */
sealed trait BotD extends
  AddressD[BotK, TopD],
  BehaviorD[`-H`[XpolarG]],
  CertificateD[BotK, BotD],
  IdentifierD[BotK],
  NatD[BotN],
  PreQuoteD[BotM],
  PulseD,
  QuoteD[BotM],
  VariantD[BotK, BotD],
  `-BagD`,
  `-BoolD`,
  `-OrderD`,
  `-RecordD`

/**
  * Labeled product type
  *
  * The type of a record is a set of key-(value-type) pairs. But we cannot represent unordered types in Scala,
  * so we instead use a list of key-(value-type) pairs. It is assumed that it is sorted by key and without duplicate,
  * so it's a canonical representation of the set of pairs.
  */
sealed trait RecordD extends TopD
sealed trait EmptyRecordD extends RecordD
sealed trait NonEmptyRecordD[
  +Key >: BotK <: TopK, +Value >: BotD <: TopD, +Tail >: `-RecordD` <: RecordD,
] extends RecordD
sealed trait `-RecordD` extends EmptyRecordD, NonEmptyRecordD[BotK, BotD, `-RecordD`]

/**
  * Labeled value
  */
sealed trait VariantD[+Key >: BotK <: TopK, +Value >: BotD <: TopD] extends TopD

/**
  * Heterogeneous unordered collection
  *
  * The type of a (heterogeneous) multiset is a multiset of data types.
  * But we cannot represent unordered types in Scala, so we instead use a list of data types.
  * It is assumed that it is sorted by data type, so it's a canonical representation of the multiset.
  */
sealed trait BagD[+Element >: BotD <: TopD] extends TopD
sealed trait EmptyBagD extends BagD[BotD]
sealed trait NonEmptyBagD[
  +TailBound >: BotD <: TopD,
  +Head >: BotD <: TopD, +Tail >: `-BagD` <: BagD[TailBound],
] extends BagD[Head | TailBound]
sealed trait `-BagD` extends EmptyBagD, NonEmptyBagD[BotD, BotD, `-BagD`]
// todo with this new version of Multiset/Bag, we need separate mirroring of D, just as for G,
//  as `-BagD` does not mirror BagD properly anymore.

/**
  * Quote that hasn't been syntactically checked
  */
sealed trait PreQuoteD[+Body >: BotM <: TopM] extends TopD

/**
  * Code as value
  */
sealed trait QuoteD[+Body >: BotM <: TopM] extends TopD

/**
  * "Compiled" quote
  */
sealed trait BehaviorD[+Behavior >: `-H`[XpolarG] <: `+H`[XpolarG]] extends TopD

/**
  * Signed value
  */
sealed trait CertificateD[+K >: BotK <: TopK, +Payload >: BotD <: TopD] extends TopD

/**
  * Name as value
  */
sealed trait IdentifierD[+K >: BotK <: TopK] extends TopD

/**
  * Channel address
  */
sealed trait AddressD[+K >: BotK <: TopK, -Data >: BotD <: TopD] extends TopD

/**
  * Natural number
  */
sealed trait NatD[+N >: BotN <: TopN] extends TopD

/**
  * Process control order
  */
sealed trait OrderD extends TopD
sealed trait KillD extends OrderD
sealed trait PauseD extends OrderD
sealed trait StartD extends OrderD
sealed trait `-OrderD` extends KillD, PauseD, StartD

/**
  * Boolean
  */
sealed trait BoolD extends TopD
sealed trait FalseD extends BoolD
sealed trait TrueD extends BoolD
sealed trait `-BoolD` extends FalseD, TrueD

/**
  * Unit
  */
sealed trait PulseD extends TopD
