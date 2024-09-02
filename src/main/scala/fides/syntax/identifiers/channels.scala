package fides.syntax.identifiers

import fides.syntax.code.Polarity.{Negative, Neutral, Positive}
import fides.syntax.code.{Polarity, Val, ValType}
import fides.syntax.identifiers.naming.{Context, Named}
import izumi.reflect.Tag

/**
  * Channel identifier
  *
  * At runtime, a channel identifier value does not keep track of its type.
  *
  * @param name not part of the abstract syntax. Only there so we can keep track of which channel is which with
  *             human-readable names.
  * @tparam P the type of the values that can come out of the channel
  * @tparam N the type of the values that can get into the channel
  */
final class Chan[+R <: Polarity, +P <: ValType, -N <: ValType] private[identifiers](
  name: String,
) extends Identifier(name), Val[Chan[R, P, N]]:
  override def toString: String = s"@$name"
end Chan
// todo consider renaming to ChannelP

/**
  * [[Channel]]`[T] <: `[[InpChan]]`[T] & `[[OutChan]]`[T]`
  */
type Channel[T <: ValType] = Chan[Neutral, T, T]
object Channel:
  def apply[T <: ValType]()(using Context, Tag[T]): Channel[T] = Named.from(new Chan(_))
  def apply[T <: ValType](name: String)(using Context, Tag[T]): Channel[T] = Named.from(new Chan(_), name)
end Channel

// todo keep track of anchors, so as to prevent dynamic receive
type InpChan[+T <: ValType] = Chan[Positive, T, Nothing]
object InpChan:
  def apply[T <: ValType]()(using Context, Tag[T]): InpChan[T] = Named.from(new Chan(_))
  def apply[T <: ValType](name: String)(using Context, Tag[T]): InpChan[T] = Named.from(new Chan(_), name)
end InpChan

type OutChan[-T <: ValType] = Chan[Negative, ValType, T]
object OutChan:
  def apply[T <: ValType]()(using Context, Tag[T]): OutChan[T] = Named.from(new Chan(_))
  def apply[T <: ValType](name: String)(using Context, Tag[T]): OutChan[T] = Named.from(new Chan(_), name)
end OutChan
