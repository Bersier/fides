package doodle

final class JumpList(val length: Int) extends Iterable[Int] {
  private[this] var elementCount = length
  private[this] val prev: Array[Int] = (-1 to length).toArray
  prev(0) = 0
  private[this] val next: Array[Int] = (1 to (length + 2)).toArray
  next(length + 1) = length + 1

  override def size: Int = elementCount

  def remove(i: Int): Unit = {
    require(i < length)
    val j: Int = i + 1
    if (prev(j) != -1) {
      next(prev(j)) = next(j)
      prev(next(j)) = prev(j)
      prev(j) = -1
      elementCount -= 1
    }
  }

  override def iterator: Iterator[Int] = new Iterator[Int] {
    private var current = JumpList.this.next(0)
    override def hasNext: Boolean = current <= JumpList.this.length

    override def next(): Int = {
      val result = current - 1
      current = JumpList.this.next(current)
      result
    }
  }
}
