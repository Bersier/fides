package fides.syntax.util

import util.{FiniteEnumerable, Trit}
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

  /**
    * @param elements a non-empty set of elements in the hierarchy
    * @return the join of [[elements]]
    */
  infix def u(elements: FiniteSet.NonEmpty[Element]): Element

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
    given (Hierarchy{ type Element = T }) = deferred

    type Constructor <: Link[T, Element]
    given FiniteEnumerable[Constructor] = deferred

    /**
      * @return all the values of type [[Constructor]], all the constructors in the hierarchy
      */
    final def constructors: FiniteSet[Constructor] = summon[FiniteEnumerable[Constructor]].values

    def root: Constructor

    def rootChildren: Multiset[Sub[T]]
  end Rooted

  final case class Extended[T](rooted: Rooted[T], child: Sub[T]) extends Rooted[T]:
    type Element = rooted.Element | child.child.Element
    type Constructor = rooted.Constructor | child.child.Constructor

    override given FiniteEnumerable[Constructor]:
      def values: FiniteSet[Constructor] =
        summon[FiniteEnumerable[rooted.Constructor]].values u summon[FiniteEnumerable[child.child.Constructor]].values

    def root: Constructor = rooted.root

    def rootChildren: Multiset[Sub[T]] =
      rooted.rootChildren u Multiset(child)

    def u(elements: FiniteSet.NonEmpty[Element]): Element = ???

    def top: Element = root(summon[Hierarchy{ type Element = T }].top)

    def sign(element: Element): Trit = ???

    extension (element: Element)
      def <=(other: Element): Boolean = ???
  end Extended

  sealed trait Link[-Domain, +Codomain] extends (Domain => Codomain):
    def withChild[D <: Domain](rooted: Rooted[D]): Sub[Codomain]

  sealed trait Sub[+Codomain]:
    val child: Rooted[?]
end Hierarchy
