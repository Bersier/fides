package fides.syntax.util

import util.{Enumerable, FiniteEnumerable, Trit}
import util.collections.extensional.{FiniteSet, Multiset}
import util.collections.generic.SimpleSet

import scala.compiletime.deferred

/**
  * [[<=]] and [[Element]] define a complete join-semilattice.
  *
  * Additionally, each element has a [[util.Trit]] sign
  * such that if e1 <= e2, then sign(e1) <= sign(e2).
  */
trait Hierarchy:

  type Element
  given Enumerable[Element] = deferred

  /**
    * @return all the elements in the hierarchy
    */
  final def elements: SimpleSet[Element] = summon[Enumerable[Element]].values

  /**
    * @param elements a non-empty set of elements in the hierarchy
    * @return the join of [[elements]]
    */
  def u(elements: FiniteSet.NonEmpty[Element]): Element

  /**
    * @return the element larger than any other element
    */
  def top: Element

  /**
    * [[Trit.Negative]] indicates an empty type
    * [[Trit.Neutral]] indicates a singleton type
    * [[Trit.Positive]] indicates any other type; basically a typical type
    *
    * @return the sign of that element
    */
  def sign(element: Element): Trit

  extension (element: Element)
    /**
      * @return true iff [[element]] is smaller or equal to [[other]]
      */
    def <=(other: Element): Boolean

end Hierarchy

object Hierarchy:

  sealed trait Rooted[T] extends Hierarchy:

    type Constructor <: Link[T, Element]
    given FiniteEnumerable[Constructor] = deferred

    /**
      * @return all the values of type [[Constructor]], all the constructors in the hierarchy
      */
    final def constructors: FiniteSet[Constructor] = summon[FiniteEnumerable[Constructor]].values

    def root: Constructor

    def rootChildren: Multiset[Sub[T]]
  end Rooted

  final case class Extended[P](rooted: Rooted[P], child: Sub[P]) extends Rooted[P]
  
  sealed trait Link[Domain, Codomain] extends (Domain => Codomain):
    def withChild(rooted: Rooted[Domain]): Sub[Codomain]
  end Link

  sealed trait Sub[Codomain]
end Hierarchy

