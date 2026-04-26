package util.collections.generic

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
