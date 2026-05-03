package fides.syntax.util

import util.collections.extensional.FiniteSet
import util.collections.generic.SimpleSet
import util.{Enumerable, FiniteEnumerable, Trit}

import scala.CanEqual.derived
import scala.compiletime.deferred

/**
  * [[<=]] and [[Element]] define a complete join-semilattice.
  *
  * Additionally, each element has a [[util.Trit]] sign
  * such that if e1 <= e2, then sign(e1) <= sign(e2).
  */
trait Hierarchy:

  /**
    * Type inhabited by exactly the elements of this hierarchy.
    */
  type Element
  given Enumerable[Element] = deferred

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

  /**
    * A hierarchy with a finite number of constructors.
    *
    * @tparam RootParamT the element type of the hierarchy on which the root constructor is parametrized
    */
  sealed trait Constructed[RootParamT] extends Hierarchy:
    given (Hierarchy{ type Element = RootParamT }) = deferred

    /**
      * Type inhabited by exactly the constructors of [[this]].
      */
    type Constructor <: Link[?, Element]
    given FiniteEnumerable[Constructor] = deferred

    /**
      * @return all the values of type [[Constructor]], all the constructors in the hierarchy
      */
    final def constructors: FiniteSet[Constructor] = summon[FiniteEnumerable[Constructor]].values

    /**
      * @return the root constructor
      */
    def root: Constructor & Link[RootParamT, Element]

    /**
      * When the root constructor is applied to the top element of the hierarchy it's parametrized on,
      * it yields the top element of [[this]] hierarchy.
      */
    def top: Element = root(summon[Hierarchy{ type Element = RootParamT }].top)

    /**
      * @return all the children of the root constructor, as well as their exact relation to the root
      */
    def rootChildren: FiniteSet[Sub[RootParamT]]
  end Constructed

  /**
    * Defines a new [[Constructed]] by merging two existing ones.
    * The root of [[sub]] is added as a new child of the root of [[main]].
    *
    * @param main the [[Constructed]] whose root is reused
    * @param sub the [[Constructed]] to be glued to the main [[Constructed]] via its root
    */
  final case class Extended[RootParamT](
    main: Constructed[RootParamT], sub: Sub[RootParamT],
  )(using Hierarchy{ type Element = RootParamT }) extends Constructed[RootParamT]:

    private given mainCanEqual: CanEqual[Element, main.Element] = derived
    private given subCanEqual: CanEqual[Element, sub.child.Element] = derived

    override given Enumerable[Element]:
      def values: SimpleSet[Element] =
        summon[Enumerable[main.Element]].values u summon[Enumerable[sub.child.Element]].values

    type Element = main.Element | sub.child.Element
    type Constructor = main.Constructor | sub.child.Constructor

    override given FiniteEnumerable[Constructor]:
      def values: FiniteSet[Constructor] =
        summon[FiniteEnumerable[main.Constructor]].values u summon[FiniteEnumerable[sub.child.Constructor]].values

    def root: Constructor & Link[RootParamT, Element] = main.root

    def rootChildren: FiniteSet[Sub[RootParamT]] =
      main.rootChildren u FiniteSet(sub)

    def u(elements: FiniteSet.NonEmpty[Element]): Element = ??? // todo use private u to implement

    private def u(e1: Element, e2: Element): Element =
      val mainValues = summon[Enumerable[main.Element]].values
      val subValues = summon[Enumerable[sub.child.Element]].values
      if mainValues.contains(e1)
      then
        if mainValues.contains(e2)
        then
          main.u(FiniteSet(e1.asInstanceOf[main.Element], e2.asInstanceOf[main.Element]))
        else
          ???
      else
        if subValues.contains(e2) then
          sub.child.u(FiniteSet(e1.asInstanceOf[sub.child.Element], e2.asInstanceOf[sub.child.Element]))
        else
          ???

    def sign(element: Element): Trit =
      if summon[Enumerable[main.Element]].values.contains(element)
      then main.sign(element.asInstanceOf[main.Element])
      else sub.child.sign(element.asInstanceOf[sub.child.Element])

    extension (element: Element)
      def <=(other: Element): Boolean =
        val mainValues = summon[Enumerable[main.Element]].values
        val subValues = summon[Enumerable[sub.child.Element]].values
        if mainValues.contains(element)
        then
          if mainValues.contains(other)
          then
            element.asInstanceOf[main.Element] <= other.asInstanceOf[main.Element]
          else
            ???
        else
          if subValues.contains(other) then
            element.asInstanceOf[sub.child.Element] <= other.asInstanceOf[sub.child.Element]
          else
            ???
  end Extended

  /**
    * Represents a subtyping relation beetween two constructors,
    * mapping elements of the hierarchy on which the sub constructor is parametrized
    * to elements of the hierarchy on which the super constructor is parametrized.
    * Exactly represents their subtyping relation.
    *
    * @tparam Domain the element type of the hierarchy on which the sub constructor is parametrized
    * @tparam Codomain the element type of the hierarchy on which the super constructor is parametrized
    */
  sealed trait Link[-Domain, +Codomain] extends (Domain => Codomain):
    def withChild[D <: Domain](rooted: Constructed[D]): Sub[Codomain]

  /**
    * Represents a subtyping relation beetween two constructors, together with the exact sub.
    *
    * @tparam Codomain the element type of the hierarchy on which the super constructor is parametrized
    */
  sealed trait Sub[+Codomain]:
    val child: Constructed[?]
end Hierarchy
