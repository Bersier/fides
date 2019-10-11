package doodle

import scala.collection.mutable

object SubMaxesExercise extends App {

  println(subParSolution(Array(8, 5, 10, 7, 9, 4, 15, 12, 90, 13), 4).toList)
  println(      solution(Array(8, 5, 10, 7, 9, 4, 15, 12, 90, 13), 4).toList)

  def solution(numbers: Array[Long], k: Int): Array[Long] = {
    require(k > 0)
    require(k <= numbers.length)

    val deque = mutable.ArrayDeque.empty[Long]
    deque.append(Long.MinValue)
    for (i <- (k - 1) to 0 by -1) {
      val current = numbers(i)
      if (deque.head <= current) {
        deque.prepend(current)
      }
    }

    val answer = Array.ofDim[Long](numbers.length - k + 1)
    answer(0) = deque.head

    for (i <- k until numbers.length) {
      if (deque.head == numbers(i - k)) deque.removeHead()
      val current = numbers(i)
      deque.removeLastWhile(_ < current)
      deque.append(current)
      answer(i - k + 1) = deque.head
    }

    answer
  }

  def subParSolution(numbers: Array[Long], k: Int): Array[Long] = {
    require(k > 0)
    require(k <= numbers.length)
    val priorityQueue = SortedMultiset.empty[Long]
    val answer = Array.ofDim[Long](numbers.length - k + 1)

    @scala.annotation.tailrec
    def loop(i: Int): Unit = if (i < numbers.length) {
      priorityQueue.rem(numbers(i - k))
      priorityQueue.add(numbers(i))
      answer(i - k + 1) = priorityQueue.max
      loop(i + 1)
    }

    priorityQueue.addAll(numbers.slice(0, k))
    answer(0) = priorityQueue.max
    loop(k)
    answer
  }

  trait SortedMultiset[A] {
    this: mutable.SortedMap[A, Long] =>
    def add(a: A): SortedMultiset.this.type = {
      if (contains(a)) this(a) = this(a) + 1
      else this(a) = 1
      this
    }

    def addAll(as: IterableOnce[A]): SortedMultiset.this.type = {
      for (a <- as) add(a)
      this
    }

    def rem(a: A): SortedMultiset.this.type = {
      if (this(a) == 1) remove(a)
      else this(a) = this(a) - 1
      this
    }

    def max: A = lastKey
  }

  object SortedMultiset {
    def empty[A](implicit ord: scala.Ordering[A]): SortedMultiset[A] = {
      new TreeMapDelegate[A, Long](mutable.SortedMap.empty) with SortedMultiset[A]
    }
  }

  class TreeMapDelegate[K, V](sortedMap: mutable.SortedMap[K, V]) extends mutable.SortedMap[K, V] {
    override def last: (K, V) = sortedMap.last

    def subtractOne(elem: K): this.type = {
      sortedMap.subtractOne(elem)
      this
    }

    def iterator: Iterator[(K, V)] = sortedMap.iterator

    def ordering: Ordering[K] = sortedMap.ordering

    def rangeImpl(from: Option[K], until: Option[K]): mutable.SortedMap[K, V] = sortedMap.rangeImpl(from, until)

    def addOne(elem: (K, V)): this.type = {
      sortedMap.addOne(elem)
      this
    }

    def iteratorFrom(start: K): Iterator[(K, V)] = sortedMap.iteratorFrom(start)

    def keysIteratorFrom(start: K): Iterator[K] = sortedMap.keysIteratorFrom(start)

    def get(key: K): Option[V] = sortedMap.get(key)
  }
}
