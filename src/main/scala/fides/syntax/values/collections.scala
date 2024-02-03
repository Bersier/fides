package fides.syntax.values

import fides.syntax.code.{Code, Expr, Val, ValQ, ValType, Xctr}
import fides.syntax.identifiers.{InpChan, OutChan}

/**
  * A value that is made up of an unordered collection of values.
  */
sealed trait Collected[T <: ValType] extends ValQ[Collected[T]], ValType:
  def elements: Iterable[Val[T]]
object Collected:
  case object None extends Collected[Nothing], ValQ[None]:
    def elements: Iterable[Val[Nothing]] = Iterable.empty[Val[Nothing]]
  type None = None.type
  final case class Some[T <: ValType](elements: Val[T]*) extends Collected[T], ValQ[Some[T]]:
    assert(elements.nonEmpty)
  end Some
end Collected

type Collected2[T <: ValType] = Collected2.None | Collected2.Some[T]
object Collected2:
  case object None extends ValQ[None], ValType:
    def elements: Iterable[Val[Nothing]] = Iterable.empty[Val[Nothing]]
  type None = None.type
  final case class Some[T <: ValType](elements: Val[T]*) extends ValQ[Some[T]], ValType:
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
final case class AddElement[T <: ValType](
  element: Code[Expr[T]],
  others: Code[Expr[Collected[T]]],
) extends Expr[Collected.Some[T]]

/**
  * (Non-deterministically) extracts one element from a Collected.
  */
final case class UnAddElement[T <: ValType](
  element: Code[Xctr[T]],
  others: Code[Xctr[Collected[T]]],
) extends Xctr[Collected.Some[T]]

/**
  * Waits for [[size]] elements from [[elementSource]], then outputs them as a Collected.
  */
final case class Collect[T <: ValType](
  elementSource: Code[Val[InpChan[T]]],
  size: Code[Expr[WholeNumber]],
) extends Expr[Collected[T]]

/**
  * Outputs the elements of a Collected to [[elementSource]], and its size to [[size]].
  */
final case class UnCollect[T <: ValType](
  elementSource: Code[Val[OutChan[T]]],
  size: Code[Xctr[WholeNumber]],
) extends Xctr[Collected[T]]
