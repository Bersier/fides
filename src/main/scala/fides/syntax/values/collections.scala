package fides.syntax.values

import fides.syntax.code.{Code, Expr, Val, ValQ, ValType, Xctr}
import fides.syntax.identifiers.{InpChan, OutChan}

/**
  * A value that is made up of an unordered collection of values.
  */
sealed trait Collected[T <: ValType] extends ValQ[Collected[T]], ValType:
  def elements: Iterable[Val[T]]
object Collected:
  case object None extends Collected[Nothing], ValQ[None], ValType:
    def elements: Iterable[Val[Nothing]] = Iterable.empty[Val[Nothing]]
  type None = None.type
  final case class Some[T <: ValType](elements: Val[T]*) extends Collected[T], ValQ[Some[T]], ValType:
    assert(elements.nonEmpty)
  end Some
end Collected

/**
  * Outputs a Collected with one element added to it.
  */
final case class AddElement[T <: ValType]
(element: Code[Expr[T]], others: Code[Expr[Collected[T]]]) extends Expr[Collected.Some[T]]

/**
  * (Non-deterministically) extracts one element from a Collected.
  */
final case class UnAddElement[T <: ValType]
(element: Code[Xctr[T]], others: Code[Xctr[Collected[T]]]) extends Xctr[Collected.Some[T]]

/**
  * Waits for @size elements from @elementSource, then outputs them as a Collected.
  */
final case class Collect[T <: ValType]
(elementSource: Code[InpChan[T]], size: Code[Expr[Integer]]) extends Expr[Collected[T]]

/**
  * Outputs the elements of a Collected to @elementSource, and its size to @size.
  */
final case class UnCollect[T <: ValType]
(elementSource: Code[OutChan[T]], size: Code[Xctr[Integer]]) extends Xctr[Collected[T]]
