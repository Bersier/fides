package fides.syntax.identifiers

import fides.syntax.code.{Code, CodeType, Expr, Val, ValType, Xctr}
import izumi.reflect.Tag

/**
  * A type of location used for channels
  *
  * Syntactically, each channel has an associated metaprogramming level.
  * A channel may only be used as an InpChan at its own metaprogramming level.
  *
  * @param name not part of the abstract syntax. Only there so we can keep track of which channel is which with
  *             human-readable names.
  * @tparam T the type of the values that transit through the channel
  */
final class Channel[T <: ValType : Tag] private(name: String) extends Location(name), Code[Channel[T]]:
  override def toString: String = s"@$name"
  /**
    * This type information is only for syntax/static purposes.
    * At runtime, a channel indentifier value does not need to keep track of its type.
    */
  def valueType: Tag[T] = summon[Tag[T]]
object Channel:
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
// todo change type of c to Code[Channel[? <: T]] (same question for OutChan)?

final case class OutChan[-T <: ValType](c: Channel[? >: T]) extends Identifier(c.name), Val[OutChan[T]]:
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

/**
  * Emits to the location referred to by [[iD]], once it has a value.
  *
  * Should really be called UnInp. But, for convenience's sake, an exception to the naming convention is made.
  *
  * Dual of Inp
  */
final case class Out[-T <: ValType](iD: Code[Val[OutChan[T]]]) extends Xctr[T]

// todo resuscitate Loc (not sure if that's the correct name)?
