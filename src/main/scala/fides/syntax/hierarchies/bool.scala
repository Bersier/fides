package fides.syntax.hierarchies

import fides.syntax.util.Hierarchy
import util.{NonEmptyFiniteSet, SimpleSet, Trit}

object Bool extends Hierarchy:
  import ElementT.*
  enum ElementT derives CanEqual:
    case Top, True, False

  def elements: SimpleSet[ElementT] = new SimpleSet[ElementT]:
    def iterator: Iterator[ElementT] = ElementT.values.iterator
  
  def u(elements: NonEmptyFiniteSet[ElementT]): ElementT =
    if elements.size > 1
    then top
    else elements.iterator.next()

  def top: ElementT = Top

  def sign(element: ElementT): Trit = element match
    case Top => Trit.Positive
    case _ => Trit.Neutral

  extension (element: ElementT)
    def <=(other: ElementT): Boolean =
      element == other || other == Top
end Bool
