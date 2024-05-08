package util

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.concurrent
import scala.annotation.targetName
import scala.compiletime.ops.int.<

object TDicts:
  opaque type ID[+I <: Int] = I // todo convert to (value) class?
  object ID:
    def newInstance: ID[Int] =
      val result = next.getAndIncrement()
      if result < 0 then throw Error("Ran out of fresh IDs")
      -result
    def newFrom(i: Int): Option[ID[i.type]] =
      assert(i >= 0)
      val available = used.put(i, ()).isEmpty
      if available then Some(i) else None
    inline def from(i: Int): ID[i.type] = i
    private val used = concurrent.TrieMap.empty[Int, Unit]
    private val next = AtomicInteger(1)
    given CanEqual[ID[Int], ID[Int]] = CanEqual.derived
  end ID

  opaque type TDict[+T <: TList[V[?]]] = T
  object TDict:
    def empty: TDict[TList.Empty] = TList.Empty
  end TDict

  extension [D <: TList[V[U]], U](self: TDict[D])
    def size: Int = self.length

    @targetName("extended")
    def +[E >: U, A <: E, I <: Int & Singleton]
    (key: ID[I], value: A)(using ContainsKey[D, I] =:= false): TDict[Extended[E, D, I, A]] =
      def extended(list: TList[V[E]]): TList[V[E]] = list match
        case TList.Empty => (key, value) :: TList.Empty
        case TList.Cons((k, v), tail) =>
          if key < k then (key, value) :: list else
          if key > k then (k, v) :: extended(tail)
          else throw AssertionError("Duplicate key")
        case _ => throw AssertionError("Impossible case; added to silence spurious warning")
      extended(self).asInstanceOf[TDict[Extended[E, D, I, A]]]

    @targetName("extendedCanThrow")
    def +![E >: U, A <: E](key: ID[Int], value: A)(using CanEqual[E, E]): TDict[TList.Cons[V[E], ?, ?]] =
      def extended(list: TList[V[E]]): TList.Cons[V[E], ?, ?] = list match
        case TList.Empty => (key, value) :: TList.Empty
        case TList.Cons((k, v), tail) =>
          if key < k then (key, value) :: list else
          if key > k then (k, v) :: extended(tail)
          else if value == v then (k, v) :: tail
          else throw AssertionError("Ambiguous key")
        case _ => throw AssertionError("Impossible case; added to silence spurious warning")
      extended(self)

    @targetName("merged")
    def ++[E >: U, S <: TList[V[E]]](other: TDict[S])(using AreDisjoint[E, D, S] =:= true): TDict[Merged[E, D, S]] =
      def merged(l1: TList[V[E]], l2: TList[V[E]]): TList[V[E]] = l1 match
        case TList.Empty => l2
        case TList.Cons((k1, v1), tail1) => l2 match
          case TList.Empty => l1
          case TList.Cons((k2, v2), tail2) =>
            if k1 < k2 then (k1, v1) :: merged(tail1, l2) else
            if k1 > k2 then (k2, v2) :: merged(l1, tail2)
            else throw AssertionError("Duplicate key")
      merged(self, other).asInstanceOf[TDict[Merged[E, D, S]]]

    @targetName("mergedCanThrow")
    def ++![E >: U](other: TDict[TList[V[E]]])(using CanEqual[E, E]): TDict[TList[V[E]]] =
      def merged(l1: TList[V[E]], l2: TList[V[E]]): TList[V[E]] = l1 match
        case TList.Empty => l2
        case TList.Cons((k1, v1), tail1) => l2 match
          case TList.Empty => l1
          case TList.Cons((k2, v2), tail2) =>
            if k1 < k2 then (k1, v1) :: merged(tail1, l2) else
            if k1 > k2 then (k2, v2) :: merged(l1, tail2)
            else if v1 == v2 then (k1, v1) :: merged(tail1, tail2)
            else throw AssertionError("Ambiguous key")
      merged(self, other)

  type Extended[E, L <: TList[V[E]], K <: Int, A <: E] <: TList.Cons[V[E], ?, ?] = L match
    case TList.Empty => TList.Cons[V[A], (K, A), TList.Empty]
    case TList.Cons[?, (k, a), tail] => K < k match
      case true => TList.Cons[V[E], (K, A), L]
      case false => k < K match
        case true => TList.Cons[V[E], (k, a), Extended[E, tail, K, A]]

  type Merged[E, L1 <: TList[V[E]], L2 <: TList[V[E]]] <: TList[V[E]] = L1 match
    case TList.Empty => L2
    case TList.Cons[?, (k1, a1), tail1] => L2 match
      case TList.Empty => L1
      case TList.Cons[?, (k2, a2), tail2] => k1 < k2 match
        case true => TList.Cons[V[E], (k1, a1), Merged[E, tail1, L2]]
        case false => k2 < k1 match
          case true => TList.Cons[V[E], (k2, a2), Merged[E, L1, tail2]]

  type ContainsKey[L <: TList[V[?]], I <: Int] <: Boolean = L match
    case TList.Empty => false
    case TList.Cons[?, (I, ?), ?] => true
    case TList.Cons[?, (Int, ?), tail] => ContainsKey[tail, I]

  type AreDisjoint[E, L1 <: TList[V[E]], L2 <: TList[V[E]]] <: Boolean = L1 match
    case TList.Empty => true
    case TList.Cons[?, (k1, ?), tail1] => L2 match
      case TList.Empty => true
      case TList.Cons[?, (k2, ?), tail2] => k1 < k2 match
        case true => AreDisjoint[E, tail1, L2]
        case false => k2 < k1 match
          case true => AreDisjoint[E, L1, tail2]
          case false => false

  private type V[T] = (Int, T)
end TDicts

def test: Unit =
  import TDicts.*
  val empty: TDict[TList.Empty] = TDict.empty
  val d1 = empty + (ID.from(0), 0)
  val d2 = d1 + (ID.from(1), 0)
  val d3 = d2 ++ (empty + (ID.from(2), 0))
