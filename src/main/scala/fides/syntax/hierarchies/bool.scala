package fides.syntax.hierarchies

import fides.syntax.util.Hierarchy
import util.collections.extensional.FiniteSet
import util.collections.generic.SimpleSet
import util.{Enumerable, Trit}

object Bool extends Hierarchy:
  import Element.*
  enum Element derives CanEqual:
    case Top, True, False

  override given Enumerable[Element]:
    def values: SimpleSet[Element] = new SimpleSet[Element]:
      def contains[U](u: U)(using CanEqual[U, Element]): Boolean =
        u.isInstanceOf[Element]

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
