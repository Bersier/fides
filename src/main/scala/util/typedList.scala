package util

import scala.annotation.targetName
import scala.collection.LinearSeq
import scala.compiletime.ops.int.+


sealed trait TList[+E] extends LinearSeq[E]:
  type Length <: Int
  protected type TListL[U] = TList[U] { type Length = TList.this.Length }
  @targetName("cons") def ::[U >: E, H <: U](head: H): TList.Cons[U, H, this.type] = TList.Cons(head, this)
  override def map[U](f: E => U): TListL[U] =
    throw new AssertionError("Implemented only to refine map's return type (otherwise, Scala won't allow it).")
  def safeZip[U](that: TListL[U]): TListL[(E, U)]

object TList:
  case object Empty extends TList[Nothing]:
    type Length = 0
    override inline def apply(i: Int): Nothing = throw new IndexOutOfBoundsException(s"Off by $i")
    @targetName("cons") override inline def ::[U, H <: U](head: H): Cons[H, H, Empty] = Cons(head, this)
    override def iterator: Iterator[Nothing] = Iterator.empty
    override def length: 0 = 0
    override def forall(p: Nothing => Boolean): true = true
    override def isEmpty: true = true
    override def head: Nothing = throw new NoSuchElementException("head of empty list")
    override def tail: Nothing = throw new NoSuchElementException("tail of empty list")
    override def map[U](f: Nothing => U): this.type = this
    def safeZip[U](that: TListL[U]): Empty = Empty
  type Empty = Empty.type

  given CanEqual[TList[?], Empty] = CanEqual.derived
  given CanEqual[Empty, TList[?]] = CanEqual.derived
  given [T](using CanEqual[T, T]): CanEqual[TList[T], TList[T]] = CanEqual.derived

  final case class Cons[+E, +H <: E, +Tail <: TList[E]](override val head: H, override val tail: Tail) extends TList[E]:
    type Length = tail.Length + 1
    override inline def apply(i: Int): E = if i == 0 then head else tail(i - 1)
    override def iterator: Iterator[E] = new Iterator[E]:
      private var underlying: TList[E] = Cons.this
      override def hasNext: Boolean = underlying match
        case _: Empty   => false
        case Cons(_, _) => true
      override def next: E = underlying match
        case _: Empty   => throw new NoSuchElementException("next on empty iterator")
        case Cons(h, t) => underlying = t; h
    override def length: Length = (tail.length + 1).asInstanceOf[Length]
    override def forall(p: E => Boolean): Boolean = p(head) && tail.forall(p)
    override def isEmpty: false = false
    override def map[U](f: E => U): TListL[U] = Cons(f(head), tail.map(f))
    def safeZip[U](other: TListL[U]): TListL[(E, U)] = other match
      case Cons(h, t) => Cons((head, h), tail.safeZip(t.asInstanceOf[tail.TListL[U]]))
      case _ => throw AssertionError("Impossible")
  end Cons
end TList
