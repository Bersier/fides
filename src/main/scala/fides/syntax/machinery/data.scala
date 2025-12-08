package fides.syntax.machinery

import typelevelnumbers.binary.Bits

/**
  * Parent type of all the Scala types that represent Fides data/value types
  */
sealed trait TopD private[machinery]()

/**
  * Lower bound for all data types
  */
@deprecated
type BotD = Nothing
// todo replace by true intersection, and use as bound everywhere appropriate

/**
  * A type greater than [[TopD]]
  *
  * Indicates an unreachable data type in contravariant position.
  */
@deprecated
type OffTopD = Any

/**
  * A type smaller than [[BotD]]
  *
  * Indicates an unreachable data type (in covariant position).
  */
@deprecated
type OffBotD = Nothing

/**
  * Data type for atomic values
  *
  * Atomic values
  *  - can be tested for equality.
  *  - cannot be decomposed.
  */
sealed trait AtomD extends TopD

final abstract class PairD[+D1 <: TopD, +D2 <: TopD] extends TopD

/**
  * The type of a record is a set of key-value pairs. But we cannot represent unordered types in Scala,
  * so we instead use a list of key-value pairs. It is assumed that it is sorted by keys,
  * so it's a canonical representation of the set of pairs.
  */
sealed trait RecordD extends TopD
final abstract class EmptyRecordD extends RecordD
final abstract class NonEmptyRecordD[+K <: TopK, +V <: TopD, +D <: RecordD] extends RecordD

final abstract class NatD[+B <: Bits] extends AtomD
type NatUD = NatD[Bits]

final abstract class QuoteD[+TM <: TopM] extends TopD

final abstract class SignedD[+D <: TopD] extends TopD

@deprecated
sealed trait OldIdentifierD[+K <: TopK] extends AtomD
@deprecated
type IdentifierUD = OldIdentifierD[TopK]
@deprecated
final abstract class IdentifierKeyD[+K <: TopK] extends AtomD
@deprecated
type IdentifierKeyUD = IdentifierKeyD[TopK]

@deprecated
final abstract class ChannelD[+K <: TopK, +InpD >: BotD, -OutD <: InpD & TopD] extends OldIdentifierD[K]
@deprecated
type ChanD[+InpD >: BotD, -OutD <: InpD & TopD] = ChannelD[TopK, InpD, OutD]
@deprecated
type InpChanD[+InpD >: BotD] = ChanD[InpD, OffBotD]
@deprecated
type OutChanD[-OutD <: TopD] = ChanD[OffTopD, OutD]

/**
  * Name as value
  */
final abstract class IdentifierD[+K <: TopK] extends AtomD

/**
  * Channel address
  */
final abstract class AddressD[+K <: TopK, D <: TopD, +P >: GenP[BotB, BotB, TopB] <: TopP] extends AtomD

/**
  * Data type for unordered uniformly-typed collections of values.
  *
  * @tparam E keeps track of whether the collection is empty
  */
final abstract class CollectedD[+E <: TopE, +D <: TopD] extends TopD
type CollectedUD[+D <: TopD] = CollectedD[TopE, D]

sealed trait OrderD extends AtomD
final abstract class KillD extends OrderD
final abstract class PauseD extends OrderD
final abstract class StartD extends OrderD

final abstract class ErrorD[+D <: TopD] extends TopD

sealed trait BoolD extends AtomD
object BoolD:
  type Not[B <: BoolD] <: BoolD = B match
    case TrueD  => FalseD
    case FalseD => TrueD
    case BoolD  => BoolD
end BoolD
final abstract class FalseD extends BoolD
final abstract class TrueD extends BoolD

final abstract class StrD extends TopD

final abstract class PulseD extends AtomD
