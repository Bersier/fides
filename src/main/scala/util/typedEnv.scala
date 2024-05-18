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
sealed trait Env[+V] extends Map[Env.IDTop, V]:
  import util.Env.{AreDisjoint, ContainsKey, Extended, ID, IDTop, Merged, ValueIn, Without}

  /**
    * @param key present in this [[Env]] whose corresponding value is to be retrieved
    * @return the value corresponding to the given key
    */
  def at[W >: V, I <: Long & Singleton](key: ID[I])(using ContainsKey[Shape, I] =:= true): ValueIn[W, Shape, I]

  /**
    * @param key known to be absent from this [[Env]]
    * @param value to associate with the given new key
    * @return a new [[Env]] that additionally contains the given key-value pair
    */
  @targetName("extended")
  def +|[W >: V, U <: W, I <: Long & Singleton](key: ID[I], value: U)
  (using ContainsKey[Shape, I] =:= false): Env[W]{ type Shape = Extended[W, Env.this.Shape, I, U] }

  /**
    * @return a new [[Env]] that additionally contains the given key-value pair
    */
  @throws[Env.AmbiguousKeyError]("if the given key is already associated with a different value")
  @targetName("extendedCanThrow")
  def +![W >: V](key: IDTop, value: W)(using CanEqual[W, W]): Env[W]

  /**
    * @param key present in [[Env]], to be removed
    * @return a new [[Env]] without the given key
    */
  @targetName("without")
  def -|[W >: V, I <: Long & Singleton](key: ID[I])
  (using ContainsKey[Shape, I] =:= true): Env[V]{ type Shape = Without[W, Env.this.Shape, I] }

  /**
    * @param that another [[Env]], whose keys are disjoint from this one
    * @return the union of the this and that [[Env]]
    */
  @targetName("merged")
  def +|+[W >: V, S2 <: TList[(Long, W)]](that: Env[W]{ type Shape = S2 })
  (using AreDisjoint[W, Shape, S2] =:= true): Env[W]{ type Shape = Merged[W, Env.this.Shape, S2] }

  /**
    * @param that another [[Env]], compatible with this one
    * @return the union of the this and that [[Env]]
    */
  @throws[Env.AmbiguousKeyError]("if the any key in the resulting Env is to be associated with two different values")
  @targetName("mergedCanThrow")
  def +!+[W >: V](that: Env[W])(using CanEqual[W, W]): Env[W]

  /**
    * Internal representation of this [[Env]]'s type
    *
    * It is sorted by (Long) key, without duplicate keys.
    */
  protected type Shape <: TList[(Long, V)]

  /**
    * Internal representation of this [[Env]]
    *
    * It is sorted by (Long) key, without duplicate keys.
  */
  protected def representation: Shape
object Env:

  /**
    * Represents identifiers.
    */
  opaque type ID[+I <: Long] = I
  object ID:

    /**
      * @return a new ID
      */
    def newInstance: IDTop =
      val result = next.getAndIncrement()
      if result < 0 then throw Error("Ran out of fresh IDs")
      -result

    /**
      * @return a new ID from the given non-negative [[Long]], if unused
      */
    def newFrom(i: Long): Option[ID[i.type]] =
      assert(i >= 0)
      val available = used.put(i, ()).isEmpty
      if available then Some(i) else None

    /**
      * @return an ID from the given non-negative [[Long]]
      */
    inline def from(i: Long): ID[i.type] =
      used(i) = (); i

    private val used = concurrent.TrieMap.empty[Long, Unit]
    private val next = AtomicInteger(1)
    given CanEqual[IDTop, IDTop] = CanEqual.derived
  end ID

  /**
    * The empty [[Env]]
    */
  val empty: Env[Nothing]{ type Shape = TList.Empty } = EnvImpl(TList.Empty)

  private final class EnvImpl[+V, S <: TList[R[V]]](protected val representation: S) extends Env[V]:
    protected type Shape = S

    def removed(key: IDTop): Env[V] = EnvImpl(without(representation)(using key))

    def updated[W >: V](key: IDTop, value: W): Env[W] =
      def updated(list: TList[R[W]]): TList.Cons[R[W], ?, ?] = list match
        case TList.Empty => (key, value) :: TList.Empty
        case TList.Cons((k, v), tail) =>
          if key < k then (key, value) :: list else
          if key > k then (k, v) :: updated(tail)
          else (key, value) :: tail
        case _ => throw AssertionError("Impossible case; added to silence spurious warning")
      EnvImpl(updated(representation))

    inline def get(key: IDTop): Option[V] = valueIn(representation)(using key)

    inline def iterator: Iterator[(IDTop, V)] = representation.iterator

    def at[W >: V, I <: Long & Singleton](key: ID[I])(using ContainsKey[S, I] =:= true): ValueIn[W, Shape, I] =
      valueIn(representation)(using key).get.asInstanceOf[ValueIn[W, S, I]]

    @targetName("extended")
    def +|[W >: V, U <: W, I <: Long & Singleton](key: ID[I], value: U)
    (using ContainsKey[S, I] =:= false): Env[W]{ type Shape = Extended[W, S, I, U] } =
      EnvImpl(extended(representation)(using key, value, (_, _) => throw AssertionError("Duplicate key")))
        .asInstanceOf[Env[W]{ type Shape = Extended[W, S, I, U] }]

    @targetName("extendedCanThrow")
    def +![W >: V](key: IDTop, value: W)(using CanEqual[W, W]): Env[W] =
      EnvImpl(extended(representation)(using key, value, _ == _))

    @targetName("without")
    def -|[W >: V, I <: Long & Singleton](key: ID[I])
    (using ContainsKey[S, I] =:= true): Env[V]{ type Shape = Without[W, S, I] } =
      EnvImpl(without(representation)(using key)).asInstanceOf[Env[V]{ type Shape = Without[W, S, I] }]

    @targetName("merged")
    def +|+[W >: V, S2 <: TList[R[W]]](that: Env[W]{ type Shape = S2 })
    (using AreDisjoint[W, S, S2] =:= true): Env[W]{ type Shape = Merged[W, S, S2] } =
      EnvImpl(merged(representation, that.representation)(using (_, _) => throw AssertionError("Duplicate key")))
        .asInstanceOf[Env[W]{ type Shape = Merged[W, S, S2] }]

    @targetName("mergedCanThrow")
    def +!+[W >: V](that: Env[W])(using CanEqual[W, W]): Env[W] =
      EnvImpl(merged(representation, that.representation)(using _ == _))
  end EnvImpl

  /**
    * @param list a list of key-value pairs, sorted by key, without duplicate keys
    * @param key whose associated value is to be returned
    * @return the value associated to the given key in the given list, if present
    */
  private def valueIn[V](list: TList[R[V]])(implicit key: Long): Option[V] = list.consOption.flatMap:
    case TList.Cons((k, v), tail) => key == k thenYield v orElse (valueIn(tail), provided = key < k)

  /**
    * @param list a list of key-value pairs, sorted by key, without duplicate keys
    * @param eq equality function for values
    * @return a new duplicate-free sorted list, additionally containing the new given key-value pair
    */
  private def extended[V](list: TList[R[V]])
  (using key: IDTop, value: V, eq: (V, V) => Boolean): TList.Cons[R[V], ?, ?] = list match
    case TList.Empty => (key, value) :: TList.Empty
    case TList.Cons((k, v), tail) =>
      if key < k then (key, value) :: list else
      if key > k then (k, v) :: extended(tail)
      else if eq(value, v) then (k, v) :: tail
      else throw AmbiguousKeyError(key)
    case _ => throw AssertionError("Impossible case; added to silence spurious warning")

  /**
    * @param list a list of key-value pairs, sorted by key, without duplicate keys
    * @return a new list, without the given key
    */
  private def without[V](list: TList[R[V]])(using key: IDTop): TList[R[V]] = list match
    case TList.Empty => list
    case TList.Cons((k, v), tail) =>
      if key < k then list else
      if key > k then (k, v) :: without(tail)
      else tail
    case _ => throw AssertionError("Impossible case; added to silence spurious warning")

  /**
    * The usual merge algorithm
    *
    * @param l1 a list of key-value pairs, sorted by key, without duplicate keys
    * @param l2 a list of key-value pairs, sorted by key, without duplicate keys
    * @param eq equality function for values
    * @return a new duplicate-free sorted list representing the union of the two given lists
    */
  private def merged[V](l1: TList[R[V]], l2: TList[R[V]])(using eq: (V, V) => Boolean): TList[R[V]] = l1 match
    case TList.Empty => l2
    case TList.Cons((k1, v1), tail1) => l2 match
      case TList.Empty => l1
      case TList.Cons((k2, v2), tail2) =>
        if k1 < k2 then (k1, v1) :: merged(tail1, l2) else
        if k1 > k2 then (k2, v2) :: merged(l1, tail2)
        else if eq(v1, v2) then (k1, v1) :: merged(tail1, tail2)
        else throw AmbiguousKeyError(k1)
      case _ => throw AssertionError("Impossible case; added to silence spurious warning")
    case _ => throw AssertionError("Impossible case; added to silence spurious warning")

  /**
    * Type-level version of [[valueIn]]
    */
  type ValueIn[V, L <: TList[R[V]], K <: Long] <: V = L match
    case TList.Cons[?, (k, v), tail] => K < k match
      case false => k < K match
        case true => ValueIn[V, tail, K]
        case false => v & V

  /**
  * Type-level version of [[extended]]
  */
  type Extended[V, L <: TList[R[V]], K <: Long, U <: V] <: TList.Cons[R[V], ?, ?] = L match
    case TList.Empty => TList.Cons[R[U], (K, U), TList.Empty]
    case TList.Cons[?, (k, v), tail] => K < k match
      case true => TList.Cons[R[V], (K, U), L]
      case false => k < K match
        case true => TList.Cons[R[V], (k, v), Extended[V, tail, K, U]]

  /**
    * Type-level version of [[without]]
    */
  type Without[V, L <: TList[R[V]], K <: Long] <: TList[R[V]] = L match
    case TList.Cons[?, (k, v), tail] => K < k match
      case true => L
      case false => k < K match
        case true => TList.Cons[R[V], (k, v), Without[V, tail, K]]
        case false => tail & TList[R[V]]

  /**
    * Type-level version of [[merged]]
    */
  type Merged[V, L1 <: TList[R[V]], L2 <: TList[R[V]]] <: TList[R[V]] = L1 match
    case TList.Empty => L2
    case TList.Cons[?, (k1, v1), tail1] => L2 match
      case TList.Empty => L1
      case TList.Cons[?, (k2, v2), tail2] => k1 < k2 match
        case true => TList.Cons[R[V], (k1, v1), Merged[V, tail1, L2]]
        case false => k2 < k1 match
          case true => TList.Cons[R[V], (k2, v2), Merged[V, L1, tail2]]

  /**
    * At the type level, checks whether the given list contains the given key.
    */
  type ContainsKey[L <: TList[R[?]], K <: Long] <: Boolean = L match
    case TList.Empty => false
    case TList.Cons[?, (k, ?), tail] => K < k match
      case true => false
      case false => k < K match
        case true => ContainsKey[tail, K]
        case false => true

  /**
    * At the type level, checks whether the two given lists have disjoint keys.
    */
  type AreDisjoint[V, L1 <: TList[R[V]], L2 <: TList[R[V]]] <: Boolean = L1 match
    case TList.Empty => true
    case TList.Cons[?, (k1, ?), tail1] => L2 match
      case TList.Empty => true
      case TList.Cons[?, (k2, ?), tail2] => k1 < k2 match
        case true => AreDisjoint[V, tail1, L2]
        case false => k2 < k1 match
          case true => AreDisjoint[V, L1, tail2]
          case false => false

  /**
    * Indicates a situation where the same key is associated with two different values.
    * @param key the ambiguous key
    */
  class AmbiguousKeyError(key: IDTop) extends Error(key.toString)

  /**
    * Supertype of all [[ID]]s
    */
  private type IDTop = ID[Long]

  private type R[+T] = (Long, T)
end Env

private def envExample: Unit =
  import Env.ID
  val d1 = Env.empty +| (ID.from(0), 0)
  val d2 = d1 +| (ID.from(1), 0)
  val d3 = d2 +|+ (Env.empty +| (ID.from(2), 0))
  val d4 = d3 -| (ID.from(0)) +| (ID.from(0), 1)
  val zero: Int = d1.at(ID.from(0))
  println((d4, zero))

  def foo1(`1`: String, `2`: List[Int]): String =
    `2`.map(i => (i + 1).toString).mkString(`1`)
  println(foo1)

  def foo2(
    args: Env[Any]{
      type Shape = TList.Cons[(Long, ?), (1L, String), TList.Cons[(Long, ?), (2L, List[Int]), TList.Empty]]
    },
  ): String =
    val `1`: String = args.at(Env.ID.from(1))
    val `2`: List[Int] = args.at(Env.ID.from(2))
    `2`.map(i => (i + 1).toString).mkString(`1`)
  println(foo2)
