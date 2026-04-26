package fides.syntax.hierarchies

import util.Trit

object Nat extends Hierarchy:
  case class ElementT(value: Option[BigInt]) derives CanEqual:
    assert(value.forall(_ >= 0))

  def elements: Set[ElementT] = ???
  
  def u(elements: Set[ElementT]): ElementT = elements.size match
    case 2 => top
    case 1 => elements.head
    case 0 => throw AssertionError("At least one element should be provided")
    case _ => throw AssertionError("Impossible case")

  def top: ElementT = ElementT(None)

  def sign(element: ElementT): Trit = element match
    case ElementT(None) => Trit.Positive
    case _ => Trit.Neutral
  
  extension (element: ElementT)
    def <=(other: ElementT): Boolean =
      element == other || other == ElementT(None)
end Nat
