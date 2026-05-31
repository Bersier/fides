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

final case class Address(name: Name, datatype: Data) extends Data

final case class Entry(key: Name, value: Data) extends Data

final case class Document(signatory: Name, contents: Data) extends Data

final case class Abstraction(mapping: AbstractRenaming | Bag, contents: Data) extends Data

final case class Quote(name: Name, code: Code) extends Data

final case class Prequote(name: Name, code: Code) extends Data

final case class Behavior(xpolar: Option[XpolarType]) extends Data

final case class AbstractBag(elementType: Data) extends Data

final case class AbstractRecord(valueType: Data) extends Data

final case class AbstractRenaming() extends Data

final case class Bag(elements: Multiset[Data]) extends Data

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
