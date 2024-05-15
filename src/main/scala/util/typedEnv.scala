package util

import java.util.concurrent.atomic.AtomicInteger
import scala.annotation.targetName
import scala.collection.concurrent
import scala.compiletime.ops.long.<

/**
  * Typeful representation of a binding environment/context
  * <br><br>
  * Similarly to how [[TList]] is a much more typeful version of [[collection.LinearSeq]], [[Env]] is a much more
  * typeful version of [[Map]]`[`[[ID]]`, ?]`.
  *
  * [[TList]], by the way, is very similar to [[Tuple]], except that it additionally keeps track of an upper bound,
  * which allows it to extend [[collection.LinearSeq]].
  *
  * A normal list only keeps track of one type argument, while a tuple keeps track of the type of each of its
  * components. For example, compare:
  * {{{
  * List(None, Some(1)): List[Option[Long]]
  * (None, Some(1)): (None, Some[Long])
  * }}}
  *
  * Similarly, a dictionary only keeps track of one key and one value type argument, while a hypothetical typeful
  * dictionary would have a type that is itself a dictionary type. For example, compare:
  * {{{
  * Map('a' -> None, 'b' -> Some(1)): Map[Char, Option[Long]]
  * {'a' -> None, 'b' -> Some(1)}: {'a' -> None, 'b' -> Some[Long]}
  * }}}
  * In the example above, `{...}` is a made up notation for typeful dictionaries, analogous to `(...)` for typeful
  * lists (aka tuples).
  * <br><br>
  * [[Env]] represents typeful dictionaries whose keys are [[ID]]s (ideally singleton types) and whose values have a
  * known upper type bound.
  */
sealed trait Env[+V] extends Map[Env.ID[Long], V]:
  import util.Env.{AreDisjoint, At, ContainsKey, Extended, ID, Merged, Without}

  def at[W >: V, I <: Long & Singleton](key: ID[I]): At[W, Shape, I] & Option[V]

  @targetName("extended")
  def +[W >: V, U <: W, I <: Long & Singleton](key: ID[I], value: U)
  (using ContainsKey[Shape, I] =:= false): Env[W]{ type Shape = Extended[W, Env.this.Shape, I, U] }

  @targetName("extendedCanThrow")
  def +![W >: V](key: ID[Long], value: W)(using CanEqual[W, W]): Env[W]

  @targetName("without")
  def -[W >: V, I <: Long & Singleton](key: ID[I])
  (using ContainsKey[Shape, I] =:= true): Env[V]{ type Shape = Without[W, Env.this.Shape, I] }

  @targetName("merged")
  def ++[W >: V, S2 <: TList[(Long, W)]](that: Env[W]{ type Shape = S2 })
  (using AreDisjoint[W, Shape, S2] =:= true): Env[W]{ type Shape = Merged[W, Env.this.Shape, S2] }

  @targetName("mergedCanThrow")
  def ++![W >: V](that: Env[W])(using CanEqual[W, W]): Env[W]

  protected type Shape <: TList[(Long, V)]
  protected def representation: Shape
object Env:
  opaque type ID[+I <: Long] = I
  object ID:
    def newInstance: ID[Long] =
      val result = next.getAndIncrement()
      if result < 0 then throw Error("Ran out of fresh IDs")
      -result
    def newFrom(i: Long): Option[ID[i.type]] =
      assert(i >= 0)
      val available = used.put(i, ()).isEmpty
      if available then Some(i) else None
    inline def from(i: Long): ID[i.type] =
      used(i) = (); i
    private val used = concurrent.TrieMap.empty[Long, Unit]
    private val next = AtomicInteger(1)
    given CanEqual[ID[Long], ID[Long]] = CanEqual.derived
  end ID

  val empty: Env[Nothing]{ type Shape = TList.Empty } = EnvImpl(TList.Empty)

  private final class EnvImpl[+V, S <: TList[R[V]]](protected val representation: S) extends Env[V]:
    protected type Shape = S

    def removed(key: ID[Long]): Env[V] = EnvImpl(without(representation)(using key))

    def updated[W >: V](key: ID[Long], value: W): Env[W] =
      def updated(list: TList[R[W]]): TList.Cons[R[W], ?, ?] = list match
        case TList.Empty => (key, value) :: TList.Empty
        case TList.Cons((k, v), tail) =>
          if key < k then (key, value) :: list else
          if key > k then (k, v) :: updated(tail)
          else (key, value) :: tail
        case _ => throw AssertionError("Impossible case; added to silence spurious warning")
      EnvImpl(updated(representation))

    inline def get(key: ID[Long]): Option[V] = valueIn(representation)(using key)

    inline def iterator: Iterator[(ID[Long], V)] = representation.iterator

    def at[W >: V, I <: Long & Singleton](key: ID[I]): At[W, S, I] & Option[V] =
      valueIn(representation)(using key).asInstanceOf[At[W, S, I] & Option[V]]

    @targetName("extended")
    def +[W >: V, U <: W, I <: Long & Singleton](key: ID[I], value: U)
    (using ContainsKey[S, I] =:= false): Env[W]{ type Shape = Extended[W, S, I, U] } =
      EnvImpl(extended(representation)(using key, value, (_, _) => throw AssertionError("Duplicate key")))
        .asInstanceOf[Env[W]{ type Shape = Extended[W, S, I, U] }]

    @targetName("extendedCanThrow")
    def +![W >: V](key: ID[Long], value: W)(using CanEqual[W, W]): Env[W] =
      EnvImpl(extended(representation)(using key, value, _ == _))

    @targetName("without")
    def -[W >: V, I <: Long & Singleton](key: ID[I])
    (using ContainsKey[S, I] =:= true): Env[V]{ type Shape = Without[W, S, I] } =
      EnvImpl(without(representation)(using key)).asInstanceOf[Env[V]{ type Shape = Without[W, S, I] }]

    @targetName("merged")
    def ++[W >: V, S2 <: TList[R[W]]](that: Env[W]{ type Shape = S2 })
    (using AreDisjoint[W, S, S2] =:= true): Env[W]{ type Shape = Merged[W, S, S2] } =
      EnvImpl(merged(representation, that.representation)(using (_, _) => throw AssertionError("Duplicate key")))
        .asInstanceOf[Env[W]{ type Shape = Merged[W, S, S2] }]

    @targetName("mergedCanThrow")
    def ++![W >: V](that: Env[W])(using CanEqual[W, W]): Env[W] =
      EnvImpl(merged(representation, that.representation)(using _ == _))
  end EnvImpl

  private def valueIn[V](list: TList[R[V]])(implicit key: Long): Option[V] = list.consOption.flatMap:
    case TList.Cons((k, v), tail) => key == k thenYield v orElse (valueIn(tail), provided = key < k)

  private def extended[V](list: TList[R[V]])
  (using key: ID[Long], value: V, eq: (V, V) => Boolean): TList.Cons[R[V], ?, ?] = list match
    case TList.Empty => (key, value) :: TList.Empty
    case TList.Cons((k, v), tail) =>
      if key < k then (key, value) :: list else
      if key > k then (k, v) :: extended(tail)
      else if eq(value, v) then (k, v) :: tail
      else throw Error("Ambiguous key")
    case _ => throw AssertionError("Impossible case; added to silence spurious warning")

  private def without[V](list: TList[R[V]])(using key: ID[Long]): TList[R[V]] = list match
    case TList.Empty => list
    case TList.Cons((k, v), tail) =>
      if key < k then list else
      if key > k then (k, v) :: without(tail)
      else tail
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

  type At[V, L <: TList[R[V]], K <: Long] <: Option[?] = L match
    case TList.Empty => None.type
    case TList.Cons[R[V], (k, v), tail] => K < k match
      case true => None.type
      case false => k < K match
        case true => At[V, tail, K]
        case false => Some[v]

  type Extended[V, L <: TList[R[V]], K <: Long, U <: V] <: TList.Cons[R[V], ?, ?] = L match
    case TList.Empty => TList.Cons[R[U], (K, U), TList.Empty]
    case TList.Cons[?, (k, v), tail] => K < k match
      case true => TList.Cons[R[V], (K, U), L]
      case false => k < K match
        case true => TList.Cons[R[V], (k, v), Extended[V, tail, K, U]]

  type Without[V, L <: TList[R[V]], K <: Long] <: TList[R[V]] = L match
    case TList.Cons[?, (k, v), tail] => K < k match
      case true => L
      case false => k < K match
        case true => TList.Cons[R[V], (k, v), Without[V, tail, K]]
        case false => tail & TList[R[V]]

  type Merged[V, L1 <: TList[R[V]], L2 <: TList[R[V]]] <: TList[R[V]] = L1 match
    case TList.Empty => L2
    case TList.Cons[?, (k1, v1), tail1] => L2 match
      case TList.Empty => L1
      case TList.Cons[?, (k2, v2), tail2] => k1 < k2 match
        case true => TList.Cons[R[V], (k1, v1), Merged[V, tail1, L2]]
        case false => k2 < k1 match
          case true => TList.Cons[R[V], (k2, v2), Merged[V, L1, tail2]]

  type ContainsKey[L <: TList[R[?]], I <: Long] <: Boolean = L match
    case TList.Empty => false
    case TList.Cons[?, (I, ?), ?] => true
    case TList.Cons[?, (Long, ?), tail] => ContainsKey[tail, I]

  type AreDisjoint[V, L1 <: TList[R[V]], L2 <: TList[R[V]]] <: Boolean = L1 match
    case TList.Empty => true
    case TList.Cons[?, (k1, ?), tail1] => L2 match
      case TList.Empty => true
      case TList.Cons[?, (k2, ?), tail2] => k1 < k2 match
        case true => AreDisjoint[V, tail1, L2]
        case false => k2 < k1 match
          case true => AreDisjoint[V, L1, tail2]
          case false => false

  private type R[+T] = (Long, T)
end Env

private def envExample: Unit =
  import Env.ID
  val d1 = Env.empty + (ID.from(0), 0)
  val d2 = d1 + (ID.from(1), 0)
  val d3 = d2 ++ (Env.empty + (ID.from(2), 0))
  val someZero: Some[Int] = d1.at(ID.from(0))
  val none: None.type = d1.at(ID.from(1))
