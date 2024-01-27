package fides.syntax.identifiers

import fides.syntax.code.{Code, Expr, Ptrn, Val, ValQ, ValType, Xctr}
import izumi.reflect.Tag

/**
  * A type of location used for channels
  *
  * @tparam T the type of the values that transit through the channel
  */
final class Channel[T <: ValType : Tag] private(name: String) extends Identifier(name), ValQ[Channel[T]]:
  override def toString: String = s"@$name"
  def valueType: Tag[T] = summon[Tag[T]]
object Channel:
  def apply[T <: ValType]()(using Context, Tag[T]): Channel[T] = Identifier.from(new Channel(_))
  def apply[T <: ValType](name: String)(using Context, Tag[T]): Channel[T] = Identifier.from(new Channel(_), name)
end Channel

/**
  * Absorbs from the location referred to by @id. Reduces to the received val after reception.
  *
  * Dual of Out
  */
final case class Inp[+T <: ValType](iD: Code[Val[Channel[? <: T]]]) extends Expr[T], Code[Inp[T]]:
  override def toString: String = s"<${internalIDString(iD)}>"
end Inp

/**
  * Emits to the location referred to by @id, once it has a value.
  *
  * Should really be called UnInp. But, for convenience's sake, an exception to the naming convention is made.
  *
  * Dual of Inp
  */
final case class Out[-T <: ValType](iD: Code[Val[Channel[? >: T]]]) extends Xctr[T], Code[Out[T]]:
  override def toString: String = s"<|${internalIDString(iD)}|>"
end Out

/**
  * Emits to the location referred to by @id, once it has a matching value.
  *
  * Matches only if the type of val to be outputted is a subtype of @T,
  * so that, when used in a pattern,
  * whenever the value that would be passed to it does not match @T, the pattern will fail.
  */
final case class OutPtrn[+T <: ValType](iD: Code[Val[Channel[? <: T]]]) extends Ptrn[T, ValType]:
  override def toString: String = s"<:${internalIDString(iD)}:>"
end OutPtrn
// todo replace by MatchType

// todo should we consider some kind of limited Broadcast capability?
