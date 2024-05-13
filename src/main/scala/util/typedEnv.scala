package util

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.concurrent
import scala.annotation.targetName
import scala.compiletime.ops.int.<

object Bindings:
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
      assert(i >= 0); used(i) = (); i
    private val used = concurrent.TrieMap.empty[Int, Unit]
    private val next = AtomicInteger(1)
    given CanEqual[ID[Int], ID[Int]] = CanEqual.derived
  end ID

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
    * List(None, Some(1)): List[Option[Int]]
    * (None, Some(1)): (None, Some[Int])
    * }}}
    *
    * Similarly, a dictionary only keeps track of one key and one value type argument, while a hypothetical typeful
    * dictionary would have a type that is itself a dictionary type. For example, compare:
    * {{{
    * Map('a' -> None, 'b' -> Some(1)): Map[Char, Option[Int]]
    * {'a' -> None, 'b' -> Some(1)}: {'a' -> None, 'b' -> Some[Int]}
    * }}}
    * In the example above, `{...}` is a made up notation for typeful dictionaries, analogous to `(...)` for typeful
    * lists (aka tuples).
    * <br><br>
    * [[Env]] represents typeful dictionaries whose keys are [[ID]]s (ideally singleton types) and whose values have a
    * known upper type bound.
    *
    * @tparam L internally, the bindings are represented as a sorted list of key-value pairs, without duplicates
    */
  opaque type Env[+L <: TList[R[?]]] = L // todo extend Map[ID[Int], L.E]
  object Env:
    def empty: Env[TList.Empty] = TList.Empty
  end Env

  extension [D <: TList[R[U]], U](self: Env[D])
    def size: Int = self.length

    def at[I <: Int & Singleton](key: ID[I]): At[U, D, I] & Option[U] =
      valueIn(self)(using key).asInstanceOf[At[U, D, I] & Option[U]]

    @targetName("atOption")
    def at(key: ID[Int]): Option[U] = valueIn(self)(using key)

    @targetName("extended")
    def +[W >: U, V <: W, I <: Int & Singleton]
    (key: ID[I], value: V)(using ContainsKey[D, I] =:= false): Env[Extended[W, D, I, V]] =
      extended(self)(using key, value, (_, _) => throw AssertionError("Duplicate key"))
        .asInstanceOf[Env[Extended[W, D, I, V]]]

    @targetName("extendedCanThrow")
    def +![W >: U, V <: W](key: ID[Int], value: V)(using CanEqual[W, W]): Env[TList.Cons[R[W], ?, ?]] =
      extended(self)(using key, value, _ == _)

    @targetName("merged")
    def ++[V >: U, S <: TList[R[V]]](other: Env[S])(using AreDisjoint[V, D, S] =:= true): Env[Merged[V, D, S]] =
      merged(self, other)(using (_, _) => throw AssertionError("Duplicate key")).asInstanceOf[Env[Merged[V, D, S]]]

    @targetName("mergedCanThrow")
    def ++![V >: U](other: Env[TList[R[V]]])(using CanEqual[V, V]): Env[TList[R[V]]] =
      merged(self, other)(using _ == _)

  private def valueIn[V](list: TList[R[V]])(implicit key: ID[Int]): Option[V] = list.consOption.flatMap:
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

  private type R[T] = (Int, T)
end Bindings

private def envExample: Unit =
  import Bindings.*
  val d1 = Env.empty + (ID.from(0), 0)
  val d2 = d1 + (ID.from(1), 0)
  val d3 = d2 ++ (Env.empty + (ID.from(2), 0))
  val someZero: Some[Int] = d1.at(ID.from(0))
  val none: None.type = d1.at(ID.from(1))
