package util.collections.generic

trait SimpleSet[+T]:
  def contains[U](u: U)(using CanEqual[U, T]): Boolean
  def iterator: Iterator[T]
  infix def u[U](that: SimpleSet[U]): SimpleSet[T | U] = new SimpleSet[T | U]:
    // todo will repeat elements when the two are not disjoint
    def contains[V](u: V)(using CanEqual[V, T | U]): Boolean =
      SimpleSet.this.contains(u) || that.contains(u)
    def iterator: Iterator[T | U] = new Iterator[T | U]:
      private var first: Iterator[T | U] = SimpleSet.this.iterator
      private var second: Iterator[T | U] = that.iterator

      override def hasNext: Boolean = first.hasNext || second.hasNext
    
      override def next(): T | U =
        if !hasNext then throw new NoSuchElementException("empty iterator")
        
        if first.hasNext then
          val result = first.next()
          
          val temp = first
          first = second
          second = temp
          
          result
        else second.next()
end SimpleSet
object SimpleSet:
  class Rules[T](set: SimpleSet[T])(using CanEqual[T, T]):
    def uniqueness(n1: Int, n2: Int): Boolean =
      set.iterator.drop(n1).nextOption() != set.iterator.drop(n2).nextOption()
  end Rules
end SimpleSet
