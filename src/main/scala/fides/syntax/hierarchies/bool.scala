package fides.syntax.hierarchies

import fides.syntax.util.Hierarchy
import util.Trit
import util.collections.extensional.FiniteSet
import util.collections.generic.SimpleSet

object Bool extends Hierarchy:
  import ElementT.*
  enum ElementT derives CanEqual:
    case Top, True, False

  def elements: SimpleSet[ElementT] = new SimpleSet[ElementT]:
    def contains[U](u: U)(using CanEqual[U, ElementT]): Boolean = u.isInstanceOf[ElementT]
    def iterator: Iterator[ElementT] = ElementT.values.iterator
  
  def u(elements: FiniteSet.NonEmpty[ElementT]): ElementT =
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
