package util.collections.generic

trait SimpleSet[+T]:
  def contains[U](u: U)(using CanEqual[U, T]): Boolean

  infix def u[U](that: SimpleSet[U]): SimpleSet[T | U] = new SimpleSet[T | U]:
    def contains[V](u: V)(using CanEqual[V, T | U]): Boolean =
      SimpleSet.this.contains(u) || that.contains(u)
