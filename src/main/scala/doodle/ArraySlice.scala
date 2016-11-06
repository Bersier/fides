package doodle

final class ArraySlice[T] private(underlying: Array[T], start: Int, end: Int) extends IndexedSeq[T] {

  override def slice(from: Int, until: Int): ArraySlice[T] = {
    new ArraySlice[T](
      underlying,
      math.min(math.max(start + from, start), end),
      math.min(math.max(start + until, start), end)
    )
  }

  override def takeWhile(p: (T) => Boolean): IndexedSeq[T] = {
    var i = start
    while (p(underlying(i)) && i < end) {
      i += 1
    }
    new ArraySlice[T](underlying, i, end)
  }

  override def drop(n: Int): ArraySlice[T] = {
    new ArraySlice[T](underlying, math.min(start + n, end), end)
  }

  override def tail = {
    super.tail
    require(start < end)
    new ArraySlice[T](underlying, start + 1, end)
  }

  override def length: Int = end - start

  override def apply(idx: Int): T = underlying(start + idx)
}

object ArraySlice {
  def apply[T](array: Array[T]): ArraySlice[T] = {
    new ArraySlice(array, 0, array.length)
  }
}