package util.collections.extensional

import util.collections.generic.SimpleSet

import scala.annotation.unchecked.uncheckedVariance

case class FiniteSet[+T] private(protected val repr: Set[T @uncheckedVariance]) extends SimpleSet[T]:
  def contains[U](u: U)(using CanEqual[U, T]): Boolean =
    repr.contains(u.asInstanceOf[T])
  def iterator: Iterator[T] = repr.iterator
  def size: Int = repr.size
  def mapped[U](f: T => U): FiniteSet[U] = new FiniteSet(repr.map(f))
  def u[U](that: FiniteSet[U]): FiniteSet[T | U] =
    new FiniteSet(this.repr ++ that.repr)
object FiniteSet:
  def apply[T](elements: T*): FiniteSet[T] =
    new FiniteSet(elements.toSet)
    
  class NonEmpty[+T](representation: Set[T @uncheckedVariance]) extends FiniteSet[T](representation):
    override def u[U](that: FiniteSet[U]): NonEmpty[T | U] =
      new NonEmpty(this.repr ++ that.repr)
  object NonEmpty:
    def apply[T](element: T, elements: T*): NonEmpty[T] =
      new NonEmpty(elements.toSet + element)
  end NonEmpty
end FiniteSet
