package fides.syntax.hierarchies

import util.Trit

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
  def elements: Set[ElementT] // todo use Cats Set?

  /**
    * @param elements a non-empty set of elements in the hierarchy
    * @return the join of [[elements]]
    */
  def u(elements: Set[ElementT]): ElementT // todo NonEmptySet

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
