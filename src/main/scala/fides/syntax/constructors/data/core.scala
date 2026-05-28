package fides.syntax.constructors.data

import fides.syntax.constructors.code.Code
import fides.syntax.util.Identifier
import util.collections.extensional.Multiset

// -------------------------------------------------------------------------------------------------
// This file contains all the Fides datatype constructors.
// -------------------------------------------------------------------------------------------------

sealed trait Data
object Data:
  final case class Top() extends Data

final case class Pulse() extends Data

final case class Bool(representation: Option[Boolean]) extends Data

final case class Name(representation: Option[Identifier]) extends Data

final case class Nat(representation: Option[BigInt]) extends Data

final case class Address(name: Option[Identifier], datatype: Data) extends Data

final case class Entry(key: Option[Identifier], value: Data) extends Data

final case class Document(signatory: Option[Identifier], contents: Data) extends Data

final case class Abstraction(mapping: Bag[Bag.Refinement.Renaming.type], contents: Data) extends Data

final case class Quote(name: Option[Identifier], code: Code) extends Data

final case class Prequote(name: Option[Identifier], code: Code) extends Data

final case class Behavior(xpolar: Option[XpolarType]) extends Data

final case class Bag[R <: Bag.Refinement]( // todo consistency conditions
  explicitElements: Multiset[Data], closed: Boolean, upperBound: Data, refinement: R,
) extends Data
object Bag:
  enum Refinement:
    case None, Set, Record, Renaming

sealed trait XpolarType
object XpolarType:
  final case class Apolar() extends XpolarType
  final case class Polar(tipe: Option[Polar.Type]) extends XpolarType
  final case class Bipolar(inpType: Option[Polar.Type], outType: Option[Polar.Type]) extends XpolarType:
    import Polar.Variance.Ntrl
    assert(inpType.map(_.variance).forall(inpType => outType.map(_.variance).forall(outType =>
      inpType == outType || inpType == Ntrl || outType == Ntrl
    )))

  object Polar:
    final case class Type(variance: Variance, tipe: Data)
    enum Variance derives CanEqual:
      case Expr, Xctr, Ntrl
