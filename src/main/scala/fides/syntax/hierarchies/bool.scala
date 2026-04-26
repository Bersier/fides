package fides.syntax.hierarchies

import util.Trit

object Bool extends Hierarchy:
  import ElementT.*
  enum ElementT derives CanEqual:
    case Top, True, False

  def elements: Set[ElementT] = ElementT.values.toSet
  
  def u(elements: Set[ElementT]): ElementT = elements.size match
    case 2 => top
    case 1 => elements.head
    case 0 => throw AssertionError("At least one element should be provided")
    case _ => throw AssertionError("Impossible case")

  def top: ElementT = Top

  def sign(element: ElementT): Trit = element match
    case Top => Trit.Positive
    case _ => Trit.Neutral

  extension (element: ElementT)
    def <=(other: ElementT): Boolean =
      element == other || other == Top
end Bool
