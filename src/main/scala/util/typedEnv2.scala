package util

import java.util.concurrent.atomic.AtomicInteger
import scala.annotation.targetName
import scala.collection.concurrent
import scala.compiletime.ops.int.<

sealed trait Env2[+V] extends Map[Env2.ID[Int], V]:
  import util.Env2.{AreDisjoint, At, ContainsKey, Extended, ID, Merged}

  def at[W >: V, I <: Int & Singleton](key: ID[I]): At[W, Shape, I] & Option[V]

  @targetName("extended")
  def +[W >: V, U <: W, I <: Int & Singleton](key: ID[I], value: U)
  (using ContainsKey[Shape, I] =:= false): Env2[W]{ type Shape = Extended[W, Env2.this.Shape, I, U] }

  @targetName("extendedCanThrow")
  def +![W >: V](key: ID[Int], value: W)(using CanEqual[W, W]): Env2[W]

  @targetName("merged")
  def ++[W >: V, S2 <: TList[(Int, W)]](that: Env2[W]{ type Shape = S2 })
  (using AreDisjoint[W, Shape, S2] =:= true): Env2[W]{ type Shape = Merged[W, Env2.this.Shape, S2] }

  @targetName("mergedCanThrow")
  def ++![W >: V](that: Env2[W])(using CanEqual[W, W]): Env2[W]

  protected type Shape <: TList[(Int, V)]
  protected def representation: Shape
object Env2:
  opaque type ID[+I <: Int] = I
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

  def empty: Env2[Nothing]{ type Shape = TList.Empty } = Env2Impl(TList.Empty)

  private final class Env2Impl[+V, S <: TList[R[V]]](protected val representation: S) extends Env2[V]:
    protected type Shape = S

    def removed(key: ID[Int]): Map[ID[Int], V] = ???

    def updated[V1 >: V](key: ID[Int], value: V1): Map[ID[Int], V1] = ???

    inline def get(key: ID[Int]): Option[V] = valueIn(representation)(using key)

    inline def iterator: Iterator[(ID[Int], V)] = representation.iterator

    def at[W >: V, I <: Int & Singleton](key: ID[I]): At[W, S, I] & Option[V] =
      valueIn(representation)(using key).asInstanceOf[At[W, S, I] & Option[V]]

    @targetName("extended")
    def +[W >: V, U <: W, I <: Int & Singleton](key: ID[I], value: U)
    (using ContainsKey[S, I] =:= false): Env2[W]{ type Shape = Extended[W, S, I, U] } =
      Env2Impl(extended(representation)(using key, value, (_, _) => throw AssertionError("Duplicate key")))
        .asInstanceOf[Env2[W]{ type Shape = Extended[W, S, I, U] }]

    @targetName("extendedCanThrow")
    def +![W >: V](key: ID[Int], value: W)(using CanEqual[W, W]): Env2[W] =
      Env2Impl(extended(representation)(using key, value, _ == _))

    @targetName("merged")
    def ++[W >: V, S2 <: TList[R[W]]](that: Env2[W]{ type Shape = S2 })
    (using AreDisjoint[W, S, S2] =:= true): Env2[W]{ type Shape = Merged[W, S, S2] } =
      Env2Impl(merged(representation, that.representation)(using (_, _) => throw AssertionError("Duplicate key")))
        .asInstanceOf[Env2[W]{ type Shape = Merged[W, S, S2] }]

    @targetName("mergedCanThrow")
    def ++![W >: V](that: Env2[W])(using CanEqual[W, W]): Env2[W] =
      Env2Impl(merged(representation, that.representation)(using _ == _))
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

  private def merged[V](l1: TList[R[V]], l2: TList[R[V]])(using eq: (V, V) => Boolean): TList[R[V]] = l1 match
    case TList.Empty => l2
    case TList.Cons((k1, v1), tail1) => l2 match
      case TList.Empty => l1
      case TList.Cons((k2, v2), tail2) =>
        if k1 < k2 then (k1, v1) :: merged(tail1, l2) else
        if k1 > k2 then (k2, v2) :: merged(l1, tail2)
        else if eq(v1, v2) then (k1, v1) :: merged(tail1, tail2)
        else throw AssertionError("Ambiguous key")
      case _ => throw AssertionError("Impossible case; added to silence spurious warning")
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

  type Merged[V, L1 <: TList[R[V]], L2 <: TList[R[V]]] <: TList[R[V]] = L1 match
    case TList.Empty => L2
    case TList.Cons[?, (k1, v1), tail1] => L2 match
      case TList.Empty => L1
      case TList.Cons[?, (k2, v2), tail2] => k1 < k2 match
        case true => TList.Cons[R[V], (k1, v1), Merged[V, tail1, L2]]
        case false => k2 < k1 match
          case true => TList.Cons[R[V], (k2, v2), Merged[V, L1, tail2]]

  type ContainsKey[L <: TList[R[?]], I <: Int] <: Boolean = L match
    case TList.Empty => false
    case TList.Cons[?, (I, ?), ?] => true
    case TList.Cons[?, (Int, ?), tail] => ContainsKey[tail, I]

  type AreDisjoint[V, L1 <: TList[R[V]], L2 <: TList[R[V]]] <: Boolean = L1 match
    case TList.Empty => true
    case TList.Cons[?, (k1, ?), tail1] => L2 match
      case TList.Empty => true
      case TList.Cons[?, (k2, ?), tail2] => k1 < k2 match
        case true => AreDisjoint[V, tail1, L2]
        case false => k2 < k1 match
          case true => AreDisjoint[V, L1, tail2]
          case false => false

  private type R[+T] = (Int, T)
end Env2

private def env2Example: Unit =
  import Env2.*
  val d1 = Env2.empty + (ID.from(0), 0)
  val d2 = d1 + (ID.from(1), 0)
  val d3 = d2 ++ (Env2.empty + (ID.from(2), 0))
  val someZero: Some[Int] = d1.at(ID.from(0))
  val none: None.type = d1.at(ID.from(1))