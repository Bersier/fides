package fides.syntax.types

/**
  * Parent type of all the Scala types that represent Fides data/value types
  */
sealed trait ValTop private[types]()

type ValBot = Nothing
// TODO replace by true intersection

type OffTop = Any
type OffBot = Nothing

/**
  * Data type for atomic values. Atomic values
  *  - can be tested for equality.
  *  - cannot be decomposed.
  */
sealed trait AtomT extends ValTop

/**
  * Data type for pairs
  */
sealed trait PairT[+T1 <: ValTop, +T2 <: ValTop] extends ValTop

sealed trait WholeNumberT extends AtomT
sealed trait NaturalNumberT extends WholeNumberT
// TODO encode numbers at type level

sealed trait QuotedT[+S <: CodeType] extends ValTop

/**
  * Data type for identifiers
  */
sealed trait IdentifierT extends AtomT

sealed trait IdentifierKeyT extends AtomT

sealed trait SignedT[+T <: ValTop] extends ValTop

/**
  * Data type for Channels
  */
sealed trait ChanT[+InpT >: ValBot, -OutT <: ValTop] extends IdentifierT

sealed trait CollectedT[+IsNonEmpty <: Boolean, +T <: ValTop] extends ValTop
type Collected[+T <: ValTop] = CollectedT[Boolean, T]

sealed trait OrderT extends AtomT
sealed trait KillT extends OrderT
sealed trait PauseT extends OrderT
sealed trait StartT extends OrderT

sealed trait ErrorT[+T <: ValTop] extends ValTop

sealed trait BoolT extends AtomT
sealed trait TrueT extends BoolT
sealed trait FalseT extends BoolT

sealed trait StrT extends ValTop

sealed trait PulseT extends AtomT
