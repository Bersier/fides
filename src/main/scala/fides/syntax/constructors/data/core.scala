package fides.syntax.constructors.data

import fides.syntax.constructors.code.Code
import fides.syntax.util.Identifier
import util.collections.extensional.Multiset

// -------------------------------------------------------------------------------------------------
// This file contains all the Fides datatype constructors.
// -------------------------------------------------------------------------------------------------

sealed trait Data

// Nullary structors

final case class Pulse() extends Data

final case class Bool(representation: Option[Boolean]) extends Data

final case class Name(representation: Option[Identifier]) extends Data // todo have name values?

final case class Nat(representation: Option[BigInt]) extends Data

final case class Address(name: Option[Identifier], datatype: Data) extends Data

// Unary structors

/**
  * @param key a name
  */
final case class Entry(key: Option[Identifier], value: Data) extends Data

final case class Document(signatory: Option[Identifier], contents: Data) extends Data

final case class Abstraction(mapping: Bag[Bag.Refinement.Renaming.type], contents: Data) extends Data

final case class Quote(name: Option[Identifier], code: Code) extends Data

final case class Prequote(name: Option[Identifier], code: Code) extends Data

final case class Behavior(xpolar: Option[XpolarType]) extends Data

// Variadic structors

final case class Bag[R <: Bag.Refinement](
  explicitElements: Multiset[Data], closed: Boolean, upperBound: Data, refinement: R,
) extends Data
object Bag:
  enum Refinement:
    case None, Set, Record, Renaming

sealed trait XpolarType
object XpolarType:
  final case class Apolar() extends XpolarType
  final case class Polar(variance: Option[Variance], tipe: Data) extends XpolarType
  final case class Bipolar() extends XpolarType // todo parameters

enum Variance:
  case Bivariant, Covariant, Contravariant, Invariant
