package util.collections.extensional

import util.collections.generic.SimpleSet

import scala.annotation.tailrec

//trait NonEmptySet[+T] extends SimpleSet[T]:
//  def size: BigInt
//  assert(iterator.nonEmpty)

trait NonEmptyOps[NonEmpty[+_]]:
  def from[T](element: T, elements: T*): NonEmpty[T]
  def union[T](m1: NonEmpty[T], m2: NonEmpty[T]): NonEmpty[T]
  extension [T](m: NonEmpty[T])
    def size: Int
    def mapped[U](f: T => U): NonEmpty[U]
end NonEmptyOps

opaque type NonEmptySet[+T] = Set[T] // todo <: SimpleSet[T]; let's stop using opaque types, they don't play nice.
object NonEmptySet:
  given NonEmptyOps[NonEmptySet]:
    def empty: NonEmptySet[Nothing] = Set.empty
    def from[T](element: T, elements: T*): NonEmptySet[T] = elements.toSet + element
    def union[T](m1: NonEmptySet[T], m2: NonEmptySet[T]): NonEmptySet[T] = m1 ++ m2
    extension [T](m: NonEmptySet[T])
      def size: Int = (m: Set[T]).size
      def mapped[U](f: T => U): NonEmptySet[U] = m.map(f)
end NonEmptySet
