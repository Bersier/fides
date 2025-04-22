package fides.syntax.values

import fides.syntax.code.Polarity.{Negative, Positive}
import fides.syntax.code.{Code, Expr, Polar, Polarity, Lit, ValTop, Xctr}
import fides.syntax.identifiers.{InpChan, OutChan}
import util.&:&

/**
  * A value that is made up of an unordered collection of values.
  */
type Collected[+T <: ValTop] = CollectedG[Boolean, T]
object Collected:
  def apply[T <: ValTop](): CollectedG[false, Nothing] = None
  def apply[T <: ValTop](first: Lit[T], others: Lit[T]*): CollectedG[true, T] = new Some(first, others*)
  case object None extends CollectedG[false, Nothing]:
    def elements: Iterable[Lit[Nothing]] = Iterable.empty
  type None = None.type
  final class Some[+T <: ValTop](first: Lit[T], others: Lit[T]*) extends CollectedG[true, T]:
    val elements: Iterable[Lit[T]] = first +: others
  end Some
end Collected

sealed trait CollectedG[+IsNonEmpty <: Boolean, +T <: ValTop] extends Lit[CollectedG[IsNonEmpty, T]], ValTop:
  def elements: Iterable[Lit[T]]
  override def toString: String = s"Collected($elements)"
end CollectedG

type Collected2[T <: ValTop] = Collected2.None | Collected2.Some[T]
object Collected2:
  case object None extends Lit[None], ValTop:
    def elements: Iterable[Lit[Nothing]] = Iterable.empty[Lit[Nothing]]
  type None = None.type
  final case class Some[T <: ValTop](elements: Lit[T]*) extends Lit[Some[T]], ValTop:
    assert(elements.nonEmpty)
  end Some
  extension [T <: ValTop](c: Collected2[T])
    inline def elements: Iterable[Lit[T]] = inline c match
      case _: None => None.elements
      case s: Some[T] => s.elements
end Collected2

/**
  * Outputs a Collected with one element added to it.
  *
  * [[AddElement]]`[T] <: `[[Expr]]`[`[[Collected.Some]]`[T]]`
  */
type AddElement[T <: ValTop] = AddElementP[Positive, T, ValTop, Collected.Some[T], ValTop]
object AddElement:
  inline def apply[T <: ValTop](
    inline element: Code[Expr[T]],
    inline others: Code[Expr[Collected[T]]],
  ): AddElement[T] = AddElementP(element, others)
end AddElement

/**
  * (Non-deterministically) extracts one element from a Collected.
  *
  * [[UnAddElement]]`[T] <: `[[Xctr]]`[`[[Collected.Some]]`[T]]`
  */
type UnAddElement[T <: ValTop] = AddElementP[Negative, Nothing, T, Nothing, Collected.Some[T]]
object UnAddElement:
  inline def apply[T <: ValTop](
    inline element: Code[Xctr[T]],
    inline others: Code[Xctr[Collected[T]]],
  ): UnAddElement[T] = AddElementP(element, others)
end UnAddElement

final case class AddElementP[
  R >: Positive & Negative <: Polarity,
  P <: N,
  N <: ValTop,
  L >: Nothing <: Collected.Some[P],
  U >: Collected.Some[N] <: ValTop,
](
  element: Code[Polar[R, P, N]],
  others: Code[Polar[R, Collected[P], Collected[N]]],
)(using
  Collected.Some[P] <:< (L | Collected.Some[Nothing]),
  (U & Collected.Some[ValTop]) <:< Collected.Some[N],
  (R =:= Positive) | ((R =:= Negative) &:& (P =:= Nothing)),
) extends Polar[R, L, U]

/**
  * Waits for [[size]] elements from [[elementSource]], then outputs them as a Collected.
  */
final case class Collect[T <: ValTop](
  elementSource: Code[Lit[InpChan[T]]],
  size: Code[Expr[WholeNumber]],
) extends Expr[Collected[T]]

/**
  * Outputs the elements of a Collected to [[elementSource]], and its size to [[size]].
  */
final case class UnCollect[T <: ValTop](
  elementSource: Code[Lit[OutChan[T]]],
  size: Code[Xctr[WholeNumber]],
) extends Xctr[Collected[T]]
// todo write CollectP, once InpChan/OutChan asymmetry has been fixed
