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

  sealed trait Constructive extends Hierarchy:

    sealed trait Constructor[
      Domain <: Hierarchy { type ElementT = E }, E,
    ] extends Mapping[Domain, E, this.type, ElementT]:

      def at(element: E): ElementT

    end Constructor

    final case class Relation[
      Domain <: Hierarchy { type ElementT = E1 }, E1,
      Codomain <: Hierarchy { type ElementT = E2 }, E2,
    ](
      subtype: Constructor[Domain, E1],
      arrow: Mapping[Domain, E1, Codomain, E2],
      supertype: Constructor[Codomain, E2],
    )

  end Constructive

  object Constructive:

    final case class WithRoot(rootHierarchy: Hierarchy, newRelations: Multiset[()]) extends Constructive:

      type ElementT = this.type

      def elements: SimpleSet[WithRoot.this.type] = ???

      def u(elements: FiniteSet.NonEmpty[WithRoot.this.type]): WithRoot.this.type = ???

      def top: WithRoot.this.type = ???

      def sign(element: WithRoot.this.type): Trit = ???

      extension (element: WithRoot.this.type)
        def <=(other: WithRoot.this.type): Boolean = ???

    end WithRoot

  end Constructive

  sealed trait Mapping[
    Domain <: Hierarchy { type ElementT = E1 }, E1,
    Codomain <: Hierarchy { type ElementT = E2 }, E2,
  ]:

    def at(element: E1): E2

  end Mapping

end Hierarchy
