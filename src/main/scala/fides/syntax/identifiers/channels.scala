package fides.syntax.identifiers

import fides.syntax.code.{Code, CodeType, Expr, Val, ValQ, ValType, Xctr}
import izumi.reflect.Tag

/**
  * A type of location used for channels
  *
  * Syntactically, each channel has an associated metaprogramming level.
  * A channel may only be used as an InpChan at its own metaprogramming level.
  *
  * @tparam T the type of the values that transit through the channel
  */
final class Channel[T <: ValType : Tag] private(name: String) extends Location(name), Code[Channel[T]]:
// todo keep track of the level to enforce this constraint, ideally with Scala types
  override def toString: String = s"@$name"
  def valueType: Tag[T] = summon[Tag[T]] // todo TypeVal? Matchable/extractable? (same for Cell)
  // todo maybe channels shouldn't keep track of their type at runtime,
  //  maybe they should just be identifiers at runtime; should they even be tagged as channels?
object Channel:
  // todo don't allow the construction of Fides code with Channels directly.
  //  Instead, channels should be represented by names. Names only have meaning when linked to declarations.
  //  Declarations are associated with some kind of scope (whether implicit or explicit).
  //  The scope of a channel cannot span multiple metaprogramming phases (except perhaps when used only as an OutChan).
  //  Names are needed anyways in the concrete syntax, and, this way, placeholders and renaming actually make sense.
  //  For the sake of metaprogramming, I think that names should be dynamically manipulable. Should they be a new class
  //  of identifiers?
  //  It would still be nice if channel identifiers, to be used as OutChans, could be known before the channel is even
  //  created via launching, so as to allow referral to the channel in existing code before the new code is launched...
  //  Dually, it would be nice if existing Outchans could be referred to from within a Quoted.
  //  This could be achieved by having the scope of channel declarations be able to leak across quote boundaries in a
  //  weakened form (as OutChans).
  //  Later, names can also be used for immutable variable declarations (a more convenent form of Spread; similar to
  //  Scala vals), and for abstractions.
  def apply[T <: ValType]()(using Context, Tag[T]): Channel[T] = Location.from(new Channel(_))
  def apply[T <: ValType](name: String)(using Context, Tag[T]): Channel[T] = Location.from(new Channel(_), name)
  given [T <: ValType]: Conversion[Channel[T], InpChan[T]] with
    def apply(c: Channel[T]): InpChan[T] = InpChan(c)
  given [T <: ValType]: Conversion[Channel[T], OutChan[T]] with
    def apply(c: Channel[T]): OutChan[T] = OutChan(c)
end Channel

/**
  * [[InpChan]]s are not Fides values, as reception is always static in Fides (i.e. no dynamic receive).
  */
final case class InpChan[+T <: ValType](c: Channel[? <: T]) extends CodeType, Code[InpChan[T]]:
  override def toString: String = c.toString
object InpChan:
  def apply[T <: ValType]()(using Context, Tag[T]): InpChan[T] = new InpChan(Channel())
  def apply[T <: ValType](name: String)(using Context, Tag[T]): InpChan[T] = new InpChan(Channel(name))
end InpChan
// todo change type of c to Code[Channel[? <: T]] (same question fro OutChan)?

final case class OutChan[-T <: ValType](c: Channel[? >: T]) extends Identifier(c.name), ValQ[OutChan[T]]:
  override def toString: String = c.toString
object OutChan:
  def apply[T <: ValType]()(using Context, Tag[T]): OutChan[T] = new OutChan(Channel())
  def apply[T <: ValType](name: String)(using Context, Tag[T]): OutChan[T] = new OutChan(Channel(name))
end OutChan

/**
  * Absorbs from the location referred to by [[iD]]. Reduces to the received val after reception.
  *
  * Dual of Out
  */
final case class Inp[+T <: ValType](iD: Code[InpChan[T]]) extends Expr[T]
// todo add "guard: Code[Val[Pulse]] = Pulse" parameter?

/**
  * Emits to the location referred to by [[iD]], once it has a value.
  *
  * Should really be called UnInp. But, for convenience's sake, an exception to the naming convention is made.
  *
  * Dual of Inp
  */
final case class Out[-T <: ValType](iD: Code[Val[OutChan[T]]]) extends Xctr[T]

// todo should we consider some kind of limited Broadcast capability?
