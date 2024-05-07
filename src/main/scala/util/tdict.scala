package util

import java.util.concurrent.atomic.AtomicInteger
import scala.annotation.targetName
import scala.compiletime.ops.int.<=

object TDicts:
  opaque type ID = Int
  object Identifier:
    def newInstance: ID =
      val result = next.getAndIncrement()
      if result == -1 then throw Error("Ran out of ints to represent identifiers.")
      result
    private val next = AtomicInteger(0)
    given CanEqual[ID, ID] = CanEqual.derived
  end Identifier

  opaque type TDict[T <: TList[V[?]]] = T
  object TDict:
    def empty: TDict[TList.Empty] = TList.Empty
  end TDict
  extension [T <: TList[V[U]], U](self: TDict[T])
    def size: Int = self.length
    @targetName("plus")
    def +[E >: U, A <: E](key: ID, value: A): TDict[Extended[E, T, key.type, A]] =
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

  type Extended[E, L <: TList[V[E]], K <: ID, A <: E] <: TList.Cons[V[E], ?, ?] = L match
    case TList.Empty => TList.Cons[V[A], (K, A), TList.Empty]
    case TList.Cons[V[E], (k, a), tail] => K <= k match
      case true  => TList.Cons[V[E], (K, A), L]
      case false => TList.Cons[V[E], (k, a), Extended[E, tail, K, A]]

  private type V[T] = (ID, T)
end TDicts

trait TDictOps[TDict]:
  extension (d: TDict)
    def size: Int

end TDictOps
