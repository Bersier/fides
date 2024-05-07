package util

import java.util.concurrent.atomic.AtomicInteger
import scala.annotation.targetName
import scala.compiletime.ops.any.==
import scala.compiletime.ops.int.<=
import scala.util.NotGiven

object TDicts:
  type ID[+I <: Int] = I // todo opaque
  object ID:
    def newInstance: ID[Int] =
      val result = next.getAndIncrement()
      if result == 0 then throw Error("Ran out of ints to represent identifiers.")
      result
    private val next = AtomicInteger(256)
    val first: ID[0] = 0
    val second: ID[1] = 1
    given CanEqual[ID[Int], ID[Int]] = CanEqual.derived
  end ID

  opaque type TDict[+T <: TList[V[?]]] = T
  object TDict:
    def empty: TDict[TList.Empty] = TList.Empty
  end TDict
  extension [T <: TList[V[U]], U](self: TDict[T])
    def size: Int = self.length
    @targetName("plus")
    def +[E >: U, A <: E](key: ID[Int], value: A)(using ContainsKey2[T, key.type] =:= false):
    TDict[Extended[E, T, key.type, A]] =
      def extended(d: TList[V[E]]): TList[V[E]] = d match
        case TList.Empty => TList.Cons((key, value), TList.Empty)
        case TList.Cons((k, v), tail) => if key <= k
          then TList.Cons((key, value), d)
          else TList.Cons((k, v), extended(tail))
        case _ => throw AssertionError("impossible case; added to silence spurious warning")
      extended(self).asInstanceOf[TDict[Extended[E, T, key.type, A]]]
    @targetName("concatenated")
    def ++[W >: U, S <: TList[V[W]]](other: TDict[S]): TDict[Merged[W, T, S]] = ???

  type Merged[E, L1 <: TList[V[E]], L2 <: TList[V[E]]] <: TList[V[E]] = L1 match
    case TList.Empty => L2
    case TList.Cons[V[E], (k1, a1), tail1] => L2 match
      case TList.Empty => L1
      case TList.Cons[V[E], (k2, a2), tail2] => k1 <= k2 match
        case true => TList.Cons[V[E], (k1, a1), Merged[E, tail1, L2]]

  type Extended[E, L <: TList[V[E]], K <: ID[Int], A <: E] <: TList.Cons[V[E], ?, ?] = L match
    case TList.Empty => TList.Cons[V[A], (K, A), TList.Empty]
    case TList.Cons[V[E], (k, a), tail] => K <= k match
      case true  => TList.Cons[V[E], (K, A), L]
      case false => TList.Cons[V[E], (k, a), Extended[E, tail, K, A]]

  type ContainsKey[L <: TList[V[?]], I <: ID[Int]] <: Boolean = L match
    case TList.Empty => false
    case TList.Cons[V[Any], (I, Any), TList[(ID[Int], Any)]] => true
    case TList.Cons[V[Any], (ID[Int], Any), tail] => ContainsKey[tail, I]

  type ContainsKey2[L <: TList[V[?]], I <: ID[Int]] <: Boolean = L match
    case TList.Empty => false
    case TList.Cons[V[Any], (j, Any), tail] => I == j match
      case true => true
      case false => ContainsKey2[tail, I]

  private type V[T] = (ID[Int], T)
end TDicts

def test: Unit =
  import TDicts.*
  val empty: TDict[TList.Empty] = TDict.empty
  val d1 = empty + (ID.first, 0)
  val d2 = d1 + (ID.second, 0)
