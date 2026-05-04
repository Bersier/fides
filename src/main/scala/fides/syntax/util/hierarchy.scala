package fides.syntax.util

import util.collections.extensional.FiniteSet
import util.collections.generic.SimpleSet
import util.{FiniteEnumerable, Trit}

import scala.compiletime.deferred
import scala.reflect.TypeTest

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
  )(using
    Hierarchy{ type Element = RootParamT },
    TypeTest[main.Element | sub.child.Element, main.Element],
    TypeTest[main.Element | sub.child.Element, sub.child.Element],
  ) extends Constructed[RootParamT]:

    type Element = main.Element | sub.child.Element
    type Constructor = main.Constructor | sub.child.Constructor

    override given FiniteEnumerable[Constructor]:
      def values: FiniteSet[Constructor] =
        summon[FiniteEnumerable[main.Constructor]].values u summon[FiniteEnumerable[sub.child.Constructor]].values

    def root: Constructor & Link[RootParamT, Element] = main.root

    def rootChildren: FiniteSet[Sub[RootParamT]] =
      main.rootChildren u FiniteSet(sub)

    def u(elements: FiniteSet.NonEmpty[Element]): Element =
      def u(elements: FiniteSet[Element]): Option[Element] =
        elements match
          case FiniteSet.NonEmpty(element, others) => u(others) match
            case Some(e2) => Some(join(element, e2))
            case None => Some(element)
          case _ => None
      u(elements).get

    private def join(e1: Element, e2: Element): Element = e1 match
      case mainElement1: main.Element => e2 match
        case mainElement2: main.Element => main.u(FiniteSet(mainElement1, mainElement2))
        case _: sub.child.Element => ???
      case subElement1: sub.child.Element => e2 match
        case subElement2: sub.child.Element => sub.child.u(FiniteSet(subElement1, subElement2))
        case _: main.Element => ???

    def sign(element: Element): Trit = element match
      case mainElement: main.Element => main.sign(mainElement)
      case subElement: sub.child.Element => sub.child.sign(subElement)

    extension (element: Element)
      def <=(other: Element): Boolean = element match
        case mainElement1: main.Element => other match
          case mainElement2: main.Element => mainElement1 <= mainElement2
          case subElement2: sub.child.Element => ??? // todo
        case subElement1: sub.child.Element => other match
          case subElement2: sub.child.Element => subElement1 <= subElement2
          case mainElement2: main.Element => ???

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
