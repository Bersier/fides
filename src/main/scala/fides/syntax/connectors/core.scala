package fides.syntax.connectors

import fides.syntax.code.Polarity.{Negative, Positive}
import fides.syntax.code.{Code, Expr, Polar, Polarity, Process, Val, ValType, Xctr}
import fides.syntax.identifiers.{Chan, InpChan, Name, OutChan}
import fides.syntax.meta.Args
import fides.syntax.values.Pulse
import util.&:&

/**
  * Absorbs from the location referred to by [[iD]]. Reduces to the received val after reception.
  *
  * Dual of [[Out]]
  */
final case class Inp[T <: ValType](iD: Code[Val[InpChan[T]]] | Code[Name[T]]) extends Expr[T]

/**
  * Emits to the location referred to by [[iD]], once it has a value.
  *
  * Should really be called `UnInp`. But, for convenience's sake, an exception to the naming convention is made.
  *
  * Dual of [[Inp]]
  */
final case class Out[T <: ValType](iD: Code[Val[OutChan[T]]] | Code[Name[T]]) extends Xctr[T]

/**
  * General [[Polar]] for input and output. Note that it can only be an [[Expr]] or a [[Xctr]].
  */
// todo
final case class LocP[R >: Positive & Negative <: Polarity, P <: N, N <: ValType](
  iD: Code[Val[Chan[R, P, N]]] | Code[Name[?]], // todo NameP[R, P, N]?
)(using (R =:= Positive) | ((R =:= Negative) &:& (P =:= Nothing))) extends Polar[R, P, N]

/**
  * A hard-coded connection between one input and one output
  *
  * Equivalent to [[Spread]]([[inp]], [[Args]]([[out]])).
  */
final case class Forward[T <: ValType](inp: Code[Expr[T]], out: Code[Xctr[T]]) extends Process

/**
  * Kind-of the dual of values.
  *
  * Aka Sink, Forget
  *
  * Can be implemented in terms of the other primitives.
  */
final case class Ignore() extends Xctr[ValType]

/**
  * Spreads a value to multiple recipients.
  */
final case class Spread[T <: ValType](value: Code[Expr[T]], recipients: Code[Args[Xctr[T]]]) extends Process

/**
  * Forwards the inputted value once signalled to do so.
  */
final case class Hold[T <: ValType](signal: Code[Expr[Pulse]], value: Code[Expr[T]]) extends Expr[T]

/**
  * Upon reception of a value, outputs a pulse. It only communicates the arrival of the value,
  * but forgets/ignores about the actual value.
  */
final case class Signal(trigger: Code[Expr[?]]) extends Expr[Pulse]

/**
  * Forwards one of the inputs. Is guaranteed to forward a value if any of the inputs yields a value.
  *
  * Another way to think about this is that it forwards the value of the expression that "first" reduces to a value.
  *
  * [[Pick]]`[T] <: `[[Expr]]`[T]`
  */
type Pick[T <: ValType] = PickP[Positive, T, ValType]
object Pick:
  inline def apply[T <: ValType](inline inputs: Code[Args.Some[Expr[T]]]): Pick[T] = PickP(inputs)

/**
  * Internal choice. Non-deterministically forwards the input to one of the outputs.
  *
  * [[UnPick]]`[T] <: `[[Xctr]]`[T]`
  */
type UnPick[T <: ValType] = PickP[Negative, Nothing, T]
object UnPick:
  inline def apply[T <: ValType](inline recipients: Code[Args.Some[Xctr[T]]]): UnPick[T] = PickP(recipients)
end UnPick

/**
  * General [[Polar]] for picking. Note that it can only be an [[Expr]] or an [[Xctr]].
  */
final case class PickP[R >: Positive & Negative <: Polarity, P <: N, N <: ValType](
  connections: Code[Args.Some[Polar[R, P, N]]],
)(using (R =:= Positive) | ((R =:= Negative) &:& (P =:= Nothing))) extends Polar[R, P, N]
