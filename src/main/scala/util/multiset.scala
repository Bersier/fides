package util

trait MultisetOps[Multiset[+_]]:
  def empty: Multiset[Nothing]
  def multiset[T](elements: T*): Multiset[T]
  def union[T](m1: Multiset[T], m2: Multiset[T]): Multiset[T]
  extension [T](m: Multiset[T])
    def mapped[U](f: T => U): Multiset[U]
  // todo sum, aggregation, reduction, equipping
end MultisetOps

object Multisets:
  opaque type Multiset[+T] = Vector[T]
  given MultisetOps[Multiset]:
    def empty: Multiset[Nothing] = Vector.empty
    def multiset[T](elements: T*): Multiset[T] = Vector(elements*)
    def union[T](m1: Multiset[T], m2: Multiset[T]): Multiset[T] = m1 ++ m2
    extension [T](m: Multiset[T])
      def mapped[U](f: T => U): Multiset[U] = m.map(f)
end Multisets
