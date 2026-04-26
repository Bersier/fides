package fides.syntax.hierarchies

import fides.syntax.hierarchies.Bool.ElementT
import fides.syntax.util.Hierarchy
import util.Trit
import util.collections.extensional.NonEmptySet
import util.collections.generic.SimpleSet

object Nat extends Hierarchy:
  case class ElementT(value: Option[BigInt]) derives CanEqual:
    assert(value.forall(_ >= 0))

  def elements: SimpleSet[ElementT] = new SimpleSet[ElementT]:
    def contains[U](u: U)(using CanEqual[U, ElementT]): Boolean = u.isInstanceOf[ElementT]
    def iterator: Iterator[ElementT] =
      Iterator.single(top) ++ Iterator.iterate(BigInt(0))(_ + 1).map(n => ElementT(Some(n)))

  def u(elements: NonEmptySet[ElementT]): ElementT =
    if elements.size > 1
    then top
    else elements.iterator.next()

  def top: ElementT = ElementT(None)

  def sign(element: ElementT): Trit = element match
    case ElementT(None) => Trit.Positive
    case _ => Trit.Neutral
  
  extension (element: ElementT)
    def <=(other: ElementT): Boolean =
      element == other || other == ElementT(None)
end Nat
