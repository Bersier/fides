package fides.syntax

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
  * Parent type of all the Scala types that represent Fides data/value types
  *
  * Fides' version of [[Any]]
  */
sealed trait TopD extends OffTopD

/**
  * Lower bound for all data types
  *
  * Fides' version of [[Nothing]]
  */
sealed trait BotD extends
  `-RecordD`, VariantD[BotK, BotD], `-MultisetD`, QuoteD[BotM], CertificateD[BotK, BotD], IdentifierD[BotK],
  AddressD[BotK, TopD], BagD[BotD], `-OrderD`, `-BoolD`, PulseD

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
sealed trait MultisetD extends TopD
sealed trait EmptyMultisetD extends MultisetD
sealed trait NonEmptyMultisetD[+Head >: BotD <: TopD, +Tail >: `-MultisetD` <: MultisetD] extends MultisetD
sealed trait `-MultisetD` extends EmptyMultisetD, NonEmptyMultisetD[BotD, `-MultisetD`]

/**
  * Code value
  */
sealed trait QuoteD[+Body >: BotM <: TopM] extends TopD

/**
  * Signed value
  */
sealed trait CertificateD[+K >: BotK <: TopK, +D >: BotD <: TopD] extends TopD

/**
  * Name as value
  */
sealed trait IdentifierD[+K >: BotK <: TopK] extends TopD

/**
  * Channel address
  */
sealed trait AddressD[+K >: BotK <: TopK, -D >: BotD <: TopD] extends TopD

/**
  * Unordered uniformly-typed collection of values
  */
sealed trait BagD[+D >: BotD <: TopD] extends TopD

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
