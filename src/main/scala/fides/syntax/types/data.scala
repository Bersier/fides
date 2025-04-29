package fides.syntax.types

/**
  * Parent type of all the Scala types that represent Fides data types
  */
trait ValTop private[syntax]()

type ValBot = Nothing
// TODO replace by true intersection

type OffTop = Any
type OffBot = Nothing

/**
  * Data type for atomic values. Atomic values
  *  - can be tested for equality.
  *  - cannot be decomposed.
  */
trait AtomT extends ValTop

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
