package util.collections.extensional

trait CollectionOps[C[+_]]:
  def from[T](elements: T*): C[T]
  extension [T](m: C[T])
    infix def u[U](m2: C[U]): C[T | U]
    def mapped[U](f: T => U): C[U]
  // todo sum, aggregation, reduction, equipping
end CollectionOps
