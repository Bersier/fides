package util.collections.extensional

opaque type Multiset[+T] = Vector[T]
object Multiset:
  given CollectionOps[Multiset]:
    def from[T](elements: T*): Multiset[T] = Vector(elements*)
    def union[T](m1: Multiset[T], m2: Multiset[T]): Multiset[T] = m1 ++ m2
    extension [T](m: Multiset[T])
      def mapped[U](f: T => U): Multiset[U] = m.map(f)
end Multiset
