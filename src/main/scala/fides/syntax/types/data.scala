package fides.syntax.types

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
sealed trait PairT[+T1 >: BotT, +T2 >: BotT] extends TopT

sealed trait WholeNumberT extends AtomT
sealed trait NaturalNumberT extends WholeNumberT
// todo encode numbers at type level (use https://github.com/Bersier/TypeLevelNumbers ?)

sealed trait QuotedT[+S <: TopS] extends TopT

/**
  * Data type for identifiers
  */
sealed trait IdentifierT extends AtomT

sealed trait IdentifierKeyT extends AtomT

sealed trait SignedT[+T <: TopT] extends TopT

/**
  * Data type for Channels
  */
sealed trait ChanT[+InpT >: BotT, -OutT <: InpT & TopT] extends IdentifierT
type InpChanT[+InpT >: BotT] = ChanT[InpT, OffBotT]
type OutChanT[-OutT <: TopT] = ChanT[OffTopT, OutT]

sealed trait CollectedT[+IsNonEmpty <: Boolean, +T >: BotT] extends TopT
type BagT[+T >: BotT] = CollectedT[Boolean, T]

sealed trait OrderT extends AtomT
sealed trait KillT extends OrderT
sealed trait PauseT extends OrderT
sealed trait StartT extends OrderT

sealed trait ErrorT[+T <: TopT] extends TopT

sealed trait BoolT extends AtomT
sealed trait TrueT extends BoolT
sealed trait FalseT extends BoolT

sealed trait StrT extends TopT

sealed trait PulseT extends AtomT
