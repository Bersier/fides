package fides.syntax.values

import fides.syntax.code.{Code, Expr, Polar, Polarity, Val, ValType, Xctr}
import fides.syntax.identifiers.{InpChan, OutChan}
import Polarity.{Negative, Positive}
import util.&:&

/**
  * A value that is made up of an unordered collection of values.
  */
type Collected[+T <: ValType] = CollectedG[Boolean, T]
object Collected:
  def apply[T <: ValType](): CollectedG[false, Nothing] = None
  def apply[T <: ValType](first: Val[T], others: Val[T]*): CollectedG[true, T] = new Some(first, others*)
  case object None extends CollectedG[false, Nothing]:
    def elements: Iterable[Val[Nothing]] = Iterable.empty
  type None = None.type
  final class Some[+T <: ValType](first: Val[T], others: Val[T]*) extends CollectedG[true, T]:
    val elements: Iterable[Val[T]] = first +: others
  end Some
end Collected

sealed trait CollectedG[+IsNonEmpty <: Boolean, +T <: ValType] extends Val[CollectedG[IsNonEmpty, T]], ValType:
  def elements: Iterable[Val[T]]
  override def toString: String = s"Collected($elements)"
end CollectedG

type Collected2[T <: ValType] = Collected2.None | Collected2.Some[T]
object Collected2:
  case object None extends Val[None], ValType:
    def elements: Iterable[Val[Nothing]] = Iterable.empty[Val[Nothing]]
  type None = None.type
  final case class Some[T <: ValType](elements: Val[T]*) extends Val[Some[T]], ValType:
    assert(elements.nonEmpty)
  end Some
  extension [T <: ValType](c: Collected2[T])
    inline def elements: Iterable[Val[T]] = inline c match
      case _: None => None.elements
      case s: Some[T] => s.elements
end Collected2

/**
  * Outputs a Collected with one element added to it.
  */
type AddElement[T <: ValType] = AddElementP[Positive, T, ValType, Collected.Some[T], ValType]
object AddElement:
  inline def apply[T <: ValType](
    inline element: Code[Expr[T]],
    inline others: Code[Expr[Collected[T]]],
  ): AddElement[T] = AddElementP(element, others)
end AddElement

/**
  * (Non-deterministically) extracts one element from a Collected.
  */
type UnAddElement[T <: ValType] = AddElementP[Negative, Nothing, T, Nothing, Collected.Some[T]]
object UnAddElement:
  inline def apply[T <: ValType](
    inline element: Code[Xctr[T]],
    inline others: Code[Xctr[Collected[T]]],
  ): UnAddElement[T] = AddElementP(element, others)
end UnAddElement

final case class AddElementP[
  R >: Positive & Negative <: Polarity,
  P <: N,
  N <: ValType,
  L >: Nothing <: Collected.Some[P],
  U >: Collected.Some[N] <: ValType,
](
  element: Code[Polar[R, P, N]],
  others: Code[Polar[R, Collected[P], Collected[N]]],
)(using
  Collected.Some[P] <:< (L | Collected.Some[Nothing]),
  (U & Collected.Some[ValType]) <:< Collected.Some[N],
  (R =:= Positive) | ((R =:= Negative) &:& (P =:= Nothing)),
) extends Polar[R, L, U]

/**
  * Waits for [[size]] elements from [[elementSource]], then outputs them as a Collected.
  */
final case class Collect[T <: ValType](
  elementSource: Code[InpChan[T]],
  size: Code[Expr[WholeNumber]],
) extends Expr[Collected[T]]

/**
  * Outputs the elements of a Collected to [[elementSource]], and its size to [[size]].
  */
final case class UnCollect[T <: ValType](
  elementSource: Code[Val[OutChan[T]]],
  size: Code[Xctr[WholeNumber]],
) extends Xctr[Collected[T]]
// todo write CollectP, once InpChan/OutChan asymmetry has been fixed
