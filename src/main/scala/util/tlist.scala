package util

import annotation.targetName
import collection.LinearSeq
import compiletime.constValue
import compiletime.ops.boolean.{&&, ||}
import compiletime.ops.int.+

sealed trait TList[+T] extends LinearSeq[T]:
  type Length <: Int
  @targetName("cons") def ::[U >: T, H <: U](head: H): TList.Cons[U, H, this.type] = TList.Cons(head, this)
  override def map[U](f: T => U): TList[U] =
    throw new AssertionError("Implemented only to refine map's return type (otherwise, Scala won't allow it).")
  // todo add type-safe zip, that doesn't silently ignore elements?

object TList:
  case object Empty extends TList[Nothing]:
    type Length = 0
    override inline def apply(i: Int): Nothing = throw new IndexOutOfBoundsException(s"Off by $i")
    @targetName("cons") override def ::[U, H <: U](head: H): Cons[H, H, Empty] = Cons(head, this)
    override def iterator: Iterator[Nothing] = Iterator.empty
    override def length: 0 = 0
    override def forall(p: Nothing => Boolean): true = true
    override def isEmpty: true = true
    override def head: Nothing = throw new NoSuchElementException("head of empty list")
    override def tail: Nothing = throw new NoSuchElementException("tail of empty list")
    override def map[U](f: Nothing => U): this.type = this
  type Empty = Empty.type

  final case class Cons[+T, +H <: T, +Tail <: TList[T]](override val head: H, override val tail: Tail) extends TList[T]:
    type Length = tail.Length + 1
    override inline def apply(i: Int): T = if i == 0 then head else tail(i - 1)
    override def iterator: Iterator[T] = new Iterator[T]:
      private var underlying: TList[T] = Cons.this
      override def hasNext: Boolean = underlying match
        case _: Empty   => false
        case Cons(_, _) => true
      override def next: T = underlying match
        case _: Empty   => throw new NoSuchElementException("next on empty iterator")
        case Cons(h, t) => underlying = t; h
    override def length: Length = (tail.length + 1).asInstanceOf[Length]
    override def forall(p: T => Boolean): Boolean = p(head) && tail.forall(p)
    override def isEmpty: false = false
    override def map[U](f: T => U): TList[U] = Cons(f(head), tail.map(f))
  end Cons
end TList

/**
  * Assumes that neither L1 nor L2 have repeated elements.
  */
type AreSameSet[L1 <: TList[?], L2 <: TList[?]] <: Boolean = AreSameSize[L1, L2] match
  case false => false
  case true => IsSubset[L1, L2]

type IsSubset[L1 <: TList[?], L2 <: TList[?]] <: Boolean = L1 match
  case TList.Empty => true
  case TList.Cons[?, h1, tail1] => Contains[L2, h1] && IsSubset[tail1, L2]

type HasRepeats[L <: TList[?]] <: Boolean = L match
  case TList.Empty => false
  case TList.Cons[?, h, tail] => Contains[tail, h] || HasRepeats[tail]

type Contains[L <: TList[?], U] <: Boolean = L match
  case TList.Empty => false
  case TList.Cons[?, U, ?] => true
  case TList.Cons[?, ?, tail] => Contains[tail, U]

type Size[L <: TList[?]] <: Int = L match
  case TList.Empty => 0
  case TList.Cons[?, ?, tail] => Size[tail] + 1

type AreSameSize[L1 <: TList[?], L2 <: TList[?]] <: Boolean = L1 match
  case TList.Empty => L2 match
    case TList.Empty => true
    case _           => false
  case TList.Cons[?, ?, tail1] => L2 match
    case TList.Empty => false
    case TList.Cons[?, ?, tail2] => AreSameSize[tail1, tail2]
