package util

trait SimpleSet[+T]:
  def contains[U](u: U)(using CanEqual[U, T]): Boolean
  def iterator: Iterator[T]
end SimpleSet
object SimpleSet:
  class Rules[T](set: SimpleSet[T])(using CanEqual[T, T]):
    def uniqueness(n1: Int, n2: Int): Boolean =
      set.iterator.drop(n1).nextOption() != set.iterator.drop(n2).nextOption()
  end Rules
end SimpleSet

trait NonEmptyFiniteSet[+T] extends SimpleSet[T]:
  def size: BigInt
  assert(iterator.nonEmpty)

trait MultisetOps[Multiset[+_]]:
  def empty: Multiset[Nothing]
  def multiset[T](elements: T*): Multiset[T]
  def union[T](m1: Multiset[T], m2: Multiset[T]): Multiset[T]
  extension [T](m: Multiset[T])
    def mapped[U](f: T => U): Multiset[U]
  // todo sum, aggregation, reduction, equipping
end MultisetOps

opaque type Multiset[+T] = Vector[T]
given MultisetOps[Multiset]:
  def empty: Multiset[Nothing] = Vector.empty
  def multiset[T](elements: T*): Multiset[T] = Vector(elements*)
  def union[T](m1: Multiset[T], m2: Multiset[T]): Multiset[T] = m1 ++ m2
  extension [T](m: Multiset[T])
    def mapped[U](f: T => U): Multiset[U] = m.map(f)
