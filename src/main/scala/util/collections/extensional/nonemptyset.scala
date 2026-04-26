package util.collections.extensional

import util.collections.generic.SimpleSet

import scala.annotation.unchecked.uncheckedVariance

case class NonEmptySet[+T] private(private val repr: Set[T @uncheckedVariance]) extends SimpleSet[T]:
  def contains[U](u: U)(using CanEqual[U, T]): Boolean =
    repr.contains(u.asInstanceOf[T])
  def iterator: Iterator[T] = repr.iterator
  def size: Int = repr.size
  def mapped[U](f: T => U): NonEmptySet[U] = new NonEmptySet(repr.map(f))
object NonEmptySet:
  def apply[T](element: T, elements: T*): NonEmptySet[T] =
    new NonEmptySet(elements.toSet + element)
  def union[T](m1: NonEmptySet[T], m2: NonEmptySet[T]): NonEmptySet[T] =
    new NonEmptySet(m1.repr ++ m2.repr)
end NonEmptySet
