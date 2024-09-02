package fides.syntax.identifiers

import fides.syntax.code.Polarity.{Negative, Positive}
import fides.syntax.code.{Code, CodeType, Expr, Polar, Polarity, Val, ValType, Xctr}
import fides.syntax.identifiers.naming.{Context, Named}
import izumi.reflect.Tag
import util.&:&

// todo add channel declarations: chan <identifier> = ...; analogous to variable declarations
//  but what about channel identifiers vs variable names?
/*
val <ptrn> = <expr>
var <name> = <expr>
chan <name>;

 */

/**
  * A type of location used for channels
  *
  * @param name not part of the abstract syntax. Only there so we can keep track of which channel is which with
  *             human-readable names.
  * @tparam T the type of the values that transit through the channel
  */
final class Channel[T <: ValType : Tag] private(
  name: String,
) extends Identifier(name), InpChan[T], OutChan[T], Val[Channel[T]]:
  override def toString: String = s"@$name"
  /**
    * This type information is only for syntax/static purposes.
    * At runtime, a channel identifier value does not need to keep track of its type.
    */
  def valueType: Tag[T] = summon[Tag[T]]
object Channel:
  def apply[T <: ValType]()(using Context, Tag[T]): Channel[T] = Named.from(new Channel(_))
  def apply[T <: ValType](name: String)(using Context, Tag[T]): Channel[T] = Named.from(new Channel(_), name)
end Channel

// todo keep track of anchors, so as to prevent dynamic receive
sealed trait InpChan[+T <: ValType] extends Identifier, Val[InpChan[T]]
sealed trait OutChan[-T <: ValType] extends Identifier, Val[OutChan[T]]

/**
  * Absorbs from the location referred to by [[iD]]. Reduces to the received val after reception.
  *
  * Dual of Out
  */
final case class Inp[T <: ValType](iD: Code[InpChan[T]]) extends Expr[T]

/**
  * Emits to the location referred to by [[iD]], once it has a value.
  *
  * Should really be called UnInp. But, for convenience's sake, an exception to the naming convention is made.
  *
  * Dual of Inp
  */
final case class Out[T <: ValType](iD: Code[Val[OutChan[T]]]) extends Xctr[T]

/**
  * General [[Polar]] for input and output. Note that it can only be an [[Expr]] or a [[Xctr]].
  */
// todo
final case class LocP[R >: Positive & Negative <: Polarity, P <: N, N <: ValType](
  iD: Code[Polar[R, P, N]], // todo
)(using (R =:= Positive) | ((R =:= Negative) &:& (P =:= Nothing))) extends Polar[R, P, N]
