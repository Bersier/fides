package fides.syntax.types

import typelevelnumbers.binary.Bits
import util.TopB

/**
  * Parent type of all the Scala types that represent Fides data/value types
  */
sealed trait TopT private[types]()

/**
  * Lower bound for all data types
  */
type BotT = Nothing
// todo replace by true intersection, and use as bound everywhere appropriate

/**
  * A type greater than [[TopT]]
  *
  * Indicates an unreachable data type in contravariant position.
  */
type OffTopT = Any

/**
  * A type smaller than [[BotT]]
  *
  * Indicates an unreachable data type (in covariant position).
  */
type OffBotT = Nothing

/**
  * Data type for atomic values
  *
  * Atomic values
  *  - can be tested for equality.
  *  - cannot be decomposed.
  */
sealed trait AtomT extends TopT

/**
  * Data type for pairs
  */
final abstract class PairT[+T1 >: BotT, +T2 >: BotT] extends TopT

/**
  * The type of a record is a set of key-value pairs. But we cannot represent unordered types in Scala,
  * so we instead use a list of key-value pairs. It is assumed that it is sorted by keys,
  * so it's a canonical representation of the set of pairs.
  */
sealed trait RecordT extends TopT
final abstract class EmptyRecordT extends RecordT
final abstract class NonEmptyRecordT[+K <: ID, +V <: TopT, +T <: RecordT] extends RecordT

final abstract class NatT[+B <: Bits] extends AtomT
type NatUT = NatT[Bits]

final abstract class QuoteT[+S <: TopS] extends TopT

final abstract class SignedT[+T <: TopT] extends TopT

/**
  * Data type for identifiers
  */
sealed trait IdentifierT[+K <: ID] extends AtomT
type IdentifierUT = IdentifierT[ID]

final abstract class IdentifierKeyT[+K <: ID] extends AtomT
type IdentifierKeyUT = IdentifierKeyT[ID]

/**
  * Data type for Channels
  */
final abstract class ChannelT[+K <: ID, +InpT >: BotT, -OutT <: InpT & TopT] extends IdentifierT[K]
type ChanT[+InpT >: BotT, -OutT <: InpT & TopT] = ChannelT[ID, InpT, OutT]
type InpChanT[+InpT >: BotT] = ChanT[InpT, OffBotT]
type OutChanT[-OutT <: TopT] = ChanT[OffTopT, OutT]

/**
  * Data type for unordered uniformly-typed collections of values.
  */
final abstract class CollectedT[+IsNonEmpty <: TopB, +T >: BotT] extends TopT
type CollectedUT[+T >: BotT] = CollectedT[TopB, T]

sealed trait OrderT extends AtomT
final abstract class KillT extends OrderT
final abstract class PauseT extends OrderT
final abstract class StartT extends OrderT

final abstract class ErrorT[+T <: TopT] extends TopT

sealed trait BoolT extends AtomT
final abstract class TrueT extends BoolT
final abstract class FalseT extends BoolT

final abstract class StrT extends TopT

final abstract class PulseT extends AtomT
