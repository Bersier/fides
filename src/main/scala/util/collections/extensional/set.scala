package util.collections.extensional

import util.collections.generic.SimpleSet

import scala.annotation.unchecked.uncheckedVariance

case class FiniteSet[+T] private(protected val repr: Set[T @uncheckedVariance]) extends SimpleSet[T]:
  def contains[U](u: U)(using CanEqual[U, T]): Boolean =
    repr.contains(u.asInstanceOf[T])
  def iterator: Iterator[T] = repr.iterator
  def size: Int = repr.size
  def mapped[U](f: T => U): FiniteSet[U] = new FiniteSet(repr.map(f))
  infix def u[U](that: FiniteSet[U]): FiniteSet[T | U] =
    new FiniteSet(this.repr ++ that.repr)
  infix def u[U](that: FiniteSet.NonEmpty[U]): FiniteSet.NonEmpty[T | U] =
    new FiniteSet.NonEmpty(this.repr ++ that.repr)
object FiniteSet:
  def apply(): FiniteSet[Nothing] =
    new FiniteSet(Set.empty)
  def apply(element: Any): NonEmpty[element.type] =
    new NonEmpty(Set(element))
  def apply(element1: Any, element2: Any): NonEmpty[element1.type | element2.type] =
    new NonEmpty(Set(element1, element2))
  def apply(element1: Any, element2: Any, element3: Any): NonEmpty[element1.type | element2.type | element3.type] =
    new NonEmpty(Set(element1, element2, element3))
  def apply[T](element: T, elements: T*): NonEmpty[T] =
    new NonEmpty(elements.toSet + element)

  class NonEmpty[+T](representation: Set[T @uncheckedVariance]) extends FiniteSet[T](representation):
    override infix def u[U](that: FiniteSet[U]): NonEmpty[T | U] =
      new NonEmpty(this.repr ++ that.repr)
  end NonEmpty
end FiniteSet
