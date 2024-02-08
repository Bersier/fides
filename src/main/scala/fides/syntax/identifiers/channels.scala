package fides.syntax.identifiers

import fides.syntax.code.{Code, Expr, Ptrn, Val, ValQ, ValType, Xctr}
import fides.syntax.values.{Pulse, TypeVal}
import izumi.reflect.Tag

sealed trait InpChan[+T <: ValType] extends Identifier, ValQ[InpChan[T]]
sealed trait OutChan[-T <: ValType] extends Identifier, ValQ[OutChan[T]]

/**
  * A type of location used for channels
  *
  * @tparam T the type of the values that transit through the channel
  */
final class Channel[T <: ValType : Tag] private(
  name: String,
) extends Identifier(name), InpChan[T], OutChan[T], ValQ[Channel[T]]:
  override def toString: String = s"@$name"
  def valueType: Tag[T] = summon[Tag[T]] // todo TypeVal? Matchable/extractable? (same for Cell)
object Channel:
  def apply[T <: ValType]()(using Context, Tag[T]): Channel[T] = Identifier.from(new Channel(_))
  def apply[T <: ValType](name: String)(using Context, Tag[T]): Channel[T] = Identifier.from(new Channel(_), name)
end Channel

// todo What happens to channel names in Inp in quoted code, when the latter is launched? Is there auto renaming?
//  Or maybe channel keys should be used when metaprogramming Inps?

/**
  * Absorbs from the location referred to by [[iD]]. Reduces to the received val after reception.
  *
  * Dual of Out
  */
final case class Inp[+T <: ValType](iD: Code[Val[InpChan[T]]]) extends Expr[T]:
  override def toString: String = s"<${internalIDString(iD)}>"
end Inp
// todo add "guard: Code[Val[Pulse]] = Pulse" parameter?

/**
  * Emits to the location referred to by [[iD]], once it has a value.
  *
  * Should really be called UnInp. But, for convenience's sake, an exception to the naming convention is made.
  *
  * Dual of Inp
  */
final case class Out[-T <: ValType](iD: Code[Val[OutChan[T]]]) extends Xctr[T]:
  override def toString: String = s"<|${internalIDString(iD)}|>"
end Out

/**
  * Matches any value of type [[T]].
  */
final case class MatchType[T <: ValType](t: Code[Val[TypeVal[T]]]) extends Ptrn[T, ValType]

// todo should we consider some kind of limited Broadcast capability?
