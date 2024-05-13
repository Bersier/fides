package util

import java.util.concurrent.atomic.AtomicInteger
import scala.annotation.targetName
import scala.collection.concurrent
import scala.compiletime.ops.int.<

sealed trait Env2[+V] extends Map[Env2.ID[Int], V]:
  import util.Env2.{ContainsKey, Extended, ID}

  protected type Shape <: TList[(Int, V)]

  @targetName("extended")
  def +[W >: V, U <: W, I <: Int & Singleton]
  (key: ID[I], value: U)(using ContainsKey[Shape, I] =:= false): Env2[W]{ type Shape = Extended[W, Shape, I, U] }
object Env2:
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
    inline def from(i: Int): ID[i.type] =
      used(i) = (); i
    private val used = concurrent.TrieMap.empty[Int, Unit]
    private val next = AtomicInteger(1)
    given CanEqual[ID[Int], ID[Int]] = CanEqual.derived
  end ID

  def empty: Env2[Nothing] = Env2Impl(TList.Empty)

  private final class Env2Impl[+V, L <: Int, S <: TList[R[V]]{ type Length = L }](representation: S) extends Env2[V]:
    protected type Shape = S

    def removed(key: ID[Int]): Map[ID[Int], V] = ???

    def updated[V1 >: V](key: ID[Int], value: V1): Map[ID[Int], V1] = ???

    inline def get(key: ID[Int]): Option[V] = valueIn(representation)(using key.asInstanceOf[Int])

    def iterator: Iterator[(ID[Int], V)] = representation.iterator.asInstanceOf[Iterator[(ID[Int], V)]]

    def at[W >: V, I <: Int & Singleton](key: ID[I]): At[W, S, I] & Option[V] =
      // todo get rid of .asInstanceOf[Int]
      valueIn(representation)(using key.asInstanceOf[Int]).asInstanceOf[At[W, S, I] & Option[V]]

    @targetName("extended")
    def +[W >: V, U <: W, I <: Int & Singleton]
    (key: ID[I], value: U)(using ContainsKey[S, I] =:= false): Env2[W]{ type Shape = Extended[W, S, I, U] } =
      extended(representation)(using key, value, (_, _) => throw AssertionError("Duplicate key"))
        .asInstanceOf[Env2[W]{ type Shape = Extended[W, S, I, U] }]
  end Env2Impl

  private def valueIn[V](list: TList[R[V]])(implicit key: Int): Option[V] = list.consOption.flatMap:
    case TList.Cons((k, v), tail) => key == k thenYield v orElse (valueIn(tail), provided = key < k)

  private def extended[V](list: TList[R[V]])
  (using key: ID[Int], value: V, eq: (V, V) => Boolean): TList.Cons[R[V], ?, ?] = list match
    case TList.Empty => (key, value) :: TList.Empty
    case TList.Cons((k, v), tail) =>
      if key < k then (key, value) :: list else
      if key > k then (k, v) :: extended(tail)
      else if eq(value, v) then (k, v) :: tail
      else throw Error("Ambiguous key")
    case _ => throw AssertionError("Impossible case; added to silence spurious warning")

  type At[V, L <: TList[R[V]], K <: Int] <: Option[?] = L match
    case TList.Empty => None.type
    case TList.Cons[R[V], (k, v), tail] => K < k match
      case true => None.type
      case false => k < K match
        case true => At[V, tail, K]
        case false => Some[v]

  type Extended[W, L <: TList[R[W]], K <: Int, V <: W] <: TList.Cons[R[W], ?, ?] = L match
    case TList.Empty => TList.Cons[R[V], (K, V), TList.Empty]
    case TList.Cons[?, (k, v), tail] => K < k match
      case true => TList.Cons[R[W], (K, V), L]
      case false => k < K match
        case true => TList.Cons[R[W], (k, v), Extended[W, tail, K, V]]

  type ContainsKey[L <: TList[R[?]], I <: Int] <: Boolean = L match
    case TList.Empty => false
    case TList.Cons[?, (I, ?), ?] => true
    case TList.Cons[?, (Int, ?), tail] => ContainsKey[tail, I]

  private type R[+T] = (Int, T)
end Env2
