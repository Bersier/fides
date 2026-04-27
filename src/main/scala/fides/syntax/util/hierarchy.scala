package fides.syntax.util

import util.Trit
import util.collections.extensional.{FiniteSet, Multiset}
import util.collections.generic.SimpleSet

/**
  * [[<=]] and [[ElementT]] define a complete join-semilattice.
  *
  * Additionally, each element has a [[util.Trit]] sign
  * such that if e1 <= e2, then sign(e1) <= sign(e2).
  */
trait Hierarchy:

  type ElementT

  /**
    * @return all the values of type [[ElementT]], all the elements in the hierarchy
    */
  def elements: SimpleSet[ElementT]

  /**
    * @param elements a non-empty set of elements in the hierarchy
    * @return the join of [[elements]]
    */
  def u(elements: FiniteSet.NonEmpty[ElementT]): ElementT

  /**
    * @return the element larger than any other element
    */
  def top: ElementT

  /**
    * @return the sign of that element
    */
  def sign(element: ElementT): Trit

  extension (element: ElementT)
    /**
      * @return true iff [[element]] is smaller or equal to [[other]]
      */
    def <=(other: ElementT): Boolean

end Hierarchy

object Hierarchy:

  sealed trait Rooted[E] extends Hierarchy:

    def apply(e: E): ElementT

  end Rooted

  object Rooted:

    final case class WithRoot[E](
      rootHierarchy: Hierarchy,
      newRelations: Multiset[(Any, Any)],
    ) extends Rooted[E]:

      def elements: SimpleSet[ElementT] = ???

      def u(elements: FiniteSet.NonEmpty[ElementT]): ElementT = ???

      def top: ElementT = ???

      def sign(element: ElementT): Trit = ???

      extension (element: ElementT)
        def <=(other: ElementT): Boolean = ???

      def apply(e: E): ElementT = ???

    end WithRoot

  end Rooted

end Hierarchy
