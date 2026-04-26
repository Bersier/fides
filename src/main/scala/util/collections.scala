package util

import java.util
import scala.annotation.tailrec

trait SetOps[SimpleSet[+_]]:
  def empty: SimpleSet[Nothing]
  def SimpleSet[T](elements: T*): SimpleSet[T]
  def union[T](m1: SimpleSet[T], m2: SimpleSet[T]): SimpleSet[T]
  extension [T](m: SimpleSet[T])
    def mapped[U](f: T => U): SimpleSet[U]
end SetOps

trait SimpleSet[+T]:
  final def iterator: Iterator[T] = new Iterator[T]:
    def hasNext: Boolean =
      peekNext().nonEmpty

    def next(): T =
      nextOption().get

    override def nextOption(): Option[T] =
      val result = peekNext()
      nextState = None
      result

    private def peekNext(): Option[T] =
      if nextState.isEmpty then
        nextState = Some(getNext())
      nextState.get

    @tailrec
    private def getNext(): Option[T] =
      unsafeIterator.nextOption() match
        case None => None
        case Some(t) =>
          if seen.containsKey(t)
          then getNext()
          else
            seen.put(t, ())
            Some(t)

    private val seen = util.IdentityHashMap[T, Unit]()
    private var nextState: Option[Option[T]] = None
  protected def unsafeIterator: Iterator[T]
end SimpleSet

trait NonEmptyFiniteSet[+T] extends SimpleSet[T]:
  def size: BigInt
  assert(iterator.nonEmpty)

trait MultisetOps[Multiset[+_]]:
  def empty: Multiset[Nothing]
  def multiset[T](elements: T*): Multiset[T]
  def union[T](m1: Multiset[T], m2: Multiset[T]): Multiset[T]
  extension [T](m: Multiset[T])
    def mapped[U](f: T => U): Multiset[U]
  // todo sum, aggregation, reduction, equipping
end MultisetOps

opaque type Multiset[+T] = Vector[T]
given MultisetOps[Multiset]:
  def empty: Multiset[Nothing] = Vector.empty
  def multiset[T](elements: T*): Multiset[T] = Vector(elements*)
  def union[T](m1: Multiset[T], m2: Multiset[T]): Multiset[T] = m1 ++ m2
  extension [T](m: Multiset[T])
    def mapped[U](f: T => U): Multiset[U] = m.map(f)
