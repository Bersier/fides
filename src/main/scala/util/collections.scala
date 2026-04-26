package util

trait SetOps[SimpleSet[+_]]:
  def empty: SimpleSet[Nothing]
  def SimpleSet[T](elements: T*): SimpleSet[T]
  def union[T](m1: SimpleSet[T], m2: SimpleSet[T]): SimpleSet[T]
  extension [T](m: SimpleSet[T])
    def mapped[U](f: T => U): SimpleSet[U]
end SetOps

trait SimpleSet[+T]:
  def contains[U](u: U)(using CanEqual[U, T]): Boolean
  def iterator: Iterator[T]
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
