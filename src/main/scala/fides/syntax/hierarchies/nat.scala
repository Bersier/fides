package fides.syntax.hierarchies

import fides.syntax.hierarchies.Bool.Element
import fides.syntax.util.Hierarchy
import util.Trit
import util.collections.extensional.FiniteSet
import util.collections.generic.SimpleSet

object Nat extends Hierarchy:
  case class Element(value: Option[BigInt]) derives CanEqual:
    assert(value.forall(_ >= 0))

  val elements: SimpleSet[Element] = new SimpleSet[Element]:
    def contains[U](u: U)(using CanEqual[U, Element]): Boolean = u.isInstanceOf[Element]
    def iterator: Iterator[Element] =
      Iterator.single(top) ++ Iterator.iterate(BigInt(0))(_ + 1).map(n => Element(Some(n)))

  def u(elements: FiniteSet.NonEmpty[Element]): Element =
    if elements.size > 1
    then top
    else elements.iterator.next()

  def top: Element = Element(None)

  def sign(element: Element): Trit = element match
    case Element(None) => Trit.Positive
    case _             => Trit.Neutral
  
  extension (element: Element)
    def <=(other: Element): Boolean =
      element == other || other == Element(None)
end Nat
