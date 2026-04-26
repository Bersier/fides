package util.collections.extensional

trait CollectionOps[C[+_]]:
  def from[T](elements: T*): C[T]
  def union[T](m1: C[T], m2: C[T]): C[T]
  extension [T](m: C[T])
    def mapped[U](f: T => U): C[U]
  // todo sum, aggregation, reduction, equipping
end CollectionOps
