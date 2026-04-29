package util.collections.extensional

opaque type Multiset[+T] = Vector[T]
object Multiset:
  def apply(): Multiset[Nothing] =
    Vector()
  def apply(element: Any): Multiset[element.type] =
    Vector(element)
  def apply(element1: Any, element2: Any): Multiset[element1.type | element2.type] =
    Vector(element1, element2)
  def apply(element1: Any, element2: Any, element3: Any): Multiset[element1.type | element2.type | element3.type] =
    Vector(element1, element2, element3)
  def apply[T](elements: T*): Multiset[T] =
    elements.toVector

  given CollectionOps[Multiset]:
    def from[T](elements: T*): Multiset[T] = Vector(elements*)
    extension [T](m: Multiset[T])
      infix def u[U](m2: Multiset[U]): Multiset[T | U] = m ++ m2
      def mapped[U](f: T => U): Multiset[U] = m.map(f)
end Multiset
