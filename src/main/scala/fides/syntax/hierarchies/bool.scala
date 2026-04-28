package fides.syntax.hierarchies

import fides.syntax.util.Hierarchy
import util.Trit
import util.collections.extensional.FiniteSet
import util.collections.generic.SimpleSet

object Bool extends Hierarchy:
  import Element.*
  enum Element derives CanEqual:
    case Top, True, False

  val elements: SimpleSet[Element] = new SimpleSet[Element]:
    def contains[U](u: U)(using CanEqual[U, Element]): Boolean = u.isInstanceOf[Element]
    def iterator: Iterator[Element] = Element.values.iterator
  
  def u(elements: FiniteSet.NonEmpty[Element]): Element =
    if elements.size > 1
    then top
    else elements.iterator.next()

  def top: Element = Top

  def sign(element: Element): Trit = element match
    case Top => Trit.Positive
    case _ => Trit.Neutral

  extension (element: Element)
    def <=(other: Element): Boolean =
      element == other || other == Top
end Bool
