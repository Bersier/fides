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
  import util.Env.{AreDisjoint, ContainsKey, Extended, ID, IDTop, KVList, Merged, ValueIn, Without}

  /**
    * @param key present in this [[Env]] whose corresponding value is to be retrieved
    * @return the value corresponding to the given key
    */
  def at[I <: Long & Singleton](key: ID[I])(using ContainsKey[Shape, I] =:= true): ValueIn[Shape, I]

  /**
    * @param key known to be absent from this [[Env]]
    * @param value to associate with the given new key
    * @return a new [[Env]] that additionally contains the given key-value pair
    */
  @targetName("extended")
  def +|[W >: V, U, I <: Long & Singleton](key: ID[I], value: U)
  (using ContainsKey[Shape, I] =:= false): Env[U | W]{ type Shape = Extended[Env.this.Shape, I, U] }

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
  def -|[I <: Long & Singleton](key: ID[I])
  (using ContainsKey[Shape, I] =:= true): Env[V]{ type Shape = Without[Env.this.Shape, I] }

  /**
    * @param that another [[Env]], whose keys are disjoint from this one
    * @return the union of the this and that [[Env]]
    */
  @targetName("merged")
  def +|+[W >: V, S2 <: KVList](that: Env[W]{ type Shape = S2 })
  (using AreDisjoint[Shape, S2] =:= true): Env[W]{ type Shape = Merged[Env.this.Shape, S2] }

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
  protected type Shape <: KVList

  /**
    * Internal representation of this [[Env]]
    *
    * It is sorted by (Long) key, without duplicate keys.
  */
  protected def representation: Shape
object Env:

  sealed trait KVList
  object KVList:
    case object Empty extends KVList
    type Empty = Empty.type
    final case class Cons[K <: Long & Singleton, +V, +T <: KVList](k: K, v: V, t: T) extends KVList

    given CanEqual[KVList, Empty] = CanEqual.derived
    given CanEqual[Empty, KVList] = CanEqual.derived
    given [H, T <: KVList](using CanEqual[H, H], CanEqual[T, T]): CanEqual[Cons[?, H, T], Cons[?, H, T]] =
      CanEqual.derived
  end KVList
  import KVList.{Cons, Empty}

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
  val empty: Env[Nothing]{ type Shape = Empty } = EnvImpl(Empty)

  private final class EnvImpl[+V, S <: KVList](protected val representation: S) extends Env[V]:
    protected type Shape = S

    def removed(key: IDTop): Env[V] = EnvImpl(without(representation)(using key))

    def updated[W >: V](key: IDTop, value: W): Env[W] =
      def updated(list: KVList): Cons[?, ?, ?] = list match
        case Empty => Cons(key, value, Empty)
        case Cons(k, v, tail) =>
          if key < k then Cons(key, value, list) else
          if key > k then Cons(k, v, updated(tail))
          else Cons(key, value, tail)
      EnvImpl(updated(representation))

    inline def get(key: IDTop): Option[V] = valueIn(representation)(using key)

    inline def iterator: Iterator[(IDTop, V)] = ??? // todo

    def at[I <: Long & Singleton](key: ID[I])(using ContainsKey[S, I] =:= true): ValueIn[Shape, I] =
      valueIn(representation)(using key).get.asInstanceOf[ValueIn[S, I]]

    @targetName("extended")
    def +|[W >: V, U, I <: Long & Singleton](key: ID[I], value: U)
    (using ContainsKey[S, I] =:= false): Env[U | W]{ type Shape = Extended[S, I, U] } =
      EnvImpl(extended(representation, key, value, (_, _) => throw AssertionError("Duplicate key")))
        .asInstanceOf[Env[U | W]{ type Shape = Extended[S, I, U] }]

    @targetName("extendedCanThrow")
    def +![W >: V](key: IDTop, value: W)(using CanEqual[W, W]): Env[W] =
      EnvImpl(extended(representation, key, value, _ == _))

    @targetName("without")
    def -|[I <: Long & Singleton](key: ID[I])
    (using ContainsKey[S, I] =:= true): Env[V]{ type Shape = Without[S, I] } =
      EnvImpl(without(representation)(using key)).asInstanceOf[Env[V]{ type Shape = Without[S, I] }]

    @targetName("merged")
    def +|+[W >: V, S2 <: KVList](that: Env[W]{ type Shape = S2 })
    (using AreDisjoint[S, S2] =:= true): Env[W]{ type Shape = Merged[S, S2] } =
      EnvImpl(merged(representation, that.representation)(using (_, _) => throw AssertionError("Duplicate key")))
        .asInstanceOf[Env[W]{ type Shape = Merged[S, S2] }]

    @targetName("mergedCanThrow")
    def +!+[W >: V](that: Env[W])(using CanEqual[W, W]): Env[W] =
      given CanEqual[Any, Any] = CanEqual.derived
      EnvImpl(merged(representation, that.representation)(using _ == _))
  end EnvImpl

  /**
    * @param list a list of key-value pairs, sorted by key, without duplicate keys
    * @param key whose associated value is to be returned
    * @return the value associated to the given key in the given list, if present
    */
  private def valueIn[V](list: KVList)(implicit key: Long): Option[V] = list match
    case Empty => None
    case Cons(k, v, tail) => key == k thenYield v.asInstanceOf[V] orElse (valueIn(tail), provided = key < k)

  /**
    * @param list a list of key-value pairs, sorted by key, without duplicate keys
    * @param eq equality function for values
    * @return a new duplicate-free sorted list, additionally containing the new given key-value pair
    */
  private def extended[V](list: KVList, key: IDTop, value: V, eq: (V, V) => Boolean): Cons[?, ?, ?] = list match
    case Empty => Cons(key, value, Empty)
    case Cons(k, v, tail) =>
      if key < k then Cons(key, value, list) else
      if key > k then Cons(k, v, extended(tail, key, value, eq))
      else if eq(value, v.asInstanceOf[V]) then Cons(k, v, tail)
      else throw AmbiguousKeyError(key)

  /**
    * @param list a list of key-value pairs, sorted by key, without duplicate keys
    * @return a new list, without the given key
    */
  private def without[V](list: KVList)(using key: IDTop): KVList = list match
    case Empty => list
    case Cons(k, v, tail) =>
      if key < k then list else
      if key > k then Cons(k, v, without(tail))
      else tail

  /**
    * The usual merge algorithm
    *
    * @param l1 a list of key-value pairs, sorted by key, without duplicate keys
    * @param l2 a list of key-value pairs, sorted by key, without duplicate keys
    * @param eq equality function for values
    * @return a new duplicate-free sorted list representing the union of the two given lists
    */
  private def merged[V](l1: KVList, l2: KVList)(using eq: (V, V) => Boolean): KVList = l1 match
    case Empty => l2
    case Cons(k1, v1, tail1) => l2 match
      case Empty => l1
      case Cons(k2, v2, tail2) =>
        if k1 < k2 then Cons(k1, v1, merged(tail1, l2)) else
        if k1 > k2 then Cons(k2, v2, merged(l1, tail2))
        else if eq(v1.asInstanceOf[V], v2.asInstanceOf[V]) then Cons(k1, v1, merged(tail1, tail2))
        else throw AmbiguousKeyError(k1)

  type EnvL[L <: KVList] = Env[?]{ type Shape = Sorted[L] }

  type EnvT[T <: Tuple] = Env[?]{ type Shape = FromTuple[T] }

  type FromTuple[T <: Tuple] <: KVList = T match
    case EmptyTuple => Empty
    case (k, v) *: tail => Extended[FromTuple[tail], k, v]

  type Sorted[L <: KVList] <: KVList = L match
    case Empty => L
    case Cons[k, v, tail] => Extended[Sorted[tail], k, v]

  /**
    * Type-level version of [[valueIn]]
    */
  type ValueIn[L <: KVList, K <: Long] = L match
    case Cons[k, v, tail] => K < k match
      case false => k < K match
        case true => ValueIn[tail, K]
        case false => v

  /**
  * Type-level version of [[extended]]
  */
  type Extended[L <: KVList, K <: Long, V] <: Cons[?, ?, ?] = L match
    case Empty => Cons[K, V, Empty]
    case Cons[k, v, tail] => K < k match
      case true => Cons[K, V, L]
      case false => k < K match
        case true => Cons[k, v, Extended[tail, K, V]]

  /**
    * Type-level version of [[without]]
    */
  type Without[L <: KVList, K <: Long] <: KVList = L match
    case Cons[k, v, tail] => K < k match
      case true => L
      case false => k < K match
        case true => Cons[k, v, Without[tail, K]]
        case false => tail & KVList

  /**
    * Type-level version of [[merged]]
    */
  type Merged[L1 <: KVList, L2 <: KVList] <: KVList = L1 match
    case Empty => L2
    case Cons[k1, v1, tail1] => L2 match
      case Empty => L1
      case Cons[k2, v2, tail2] => k1 < k2 match
        case true => Cons[k1, v1, Merged[tail1, L2]]
        case false => k2 < k1 match
          case true => Cons[k2, v2, Merged[L1, tail2]]

  /**
    * At the type level, checks whether the given list contains the given key.
    */
  type ContainsKey[L <: KVList, K <: Long] <: Boolean = L match
    case Empty => false
    case Cons[k, ?, tail] => K < k match
      case true => false
      case false => k < K match
        case true => ContainsKey[tail, K]
        case false => true

  /**
    * At the type level, checks whether the two given lists have disjoint keys.
    */
  type AreDisjoint[L1 <: KVList, L2 <: KVList] <: Boolean = L1 match
    case Empty => true
    case Cons[k1, ?, tail1] => L2 match
      case Empty => true
      case Cons[k2, ?, tail2] => k1 < k2 match
        case true => AreDisjoint[tail1, L2]
        case false => k2 < k1 match
          case true => AreDisjoint[L1, tail2]
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
end Env

private def envExample: Unit =
  import Env.ID
  val d1 = Env.empty +| (ID.from(0), 0)
  val d2 = d1 +| (ID.from(1), 0)
  val d3 = d2 +|+ (Env.empty +| (ID.from(2), 0))
  val d4 = d3 -| (ID.from(0)) +| (ID.from(0), 1)
  val zero: Int = d1.at(ID.from(0))
  println((d4, zero))

  def foo1(`2`: List[Int], `1`: String): String =
    `2`.map(i => (i + 1).toString).mkString(`1`)
  println(foo1(`2` = List(1, 2), `1` = ", "))

  def foo2(args: Env.EnvT[((2L, List[Int]), (1L, String))]): String =
    val `1`: String = args.at(ID.from(1))
    val `2`: List[Int] = args.at(ID.from(2))
    `2`.map(i => (i + 1).toString).mkString(`1`)

  // todo why does Scala think that comma is of type (String | List[Int])?
  // todo it seems to instantiate the type of comma to W.
  //  If we didn't keep track of upper bounds, this should not be possible.
  println(foo2(Env.empty +| (ID.from(2), List(1, 2)) +| (ID.from(1), ", ")))
