package fides.syntax.connectors

import fides.syntax.types.*
import util.TopB

/**
  * Absorbs from the location referred to by [[iD]]. Reduces to the received val after reception.
  *
  * Dual of [[Out]]
  */
object Inp:
  def apply[T <: TopT](iD: OldCode[Cnst[ChanT[T, OffBotT]]]): OldCode[Exvr[T]] = Loc(iD)
end Inp
// todo add variance, like here, to all primitives, for the sake of metaprogramming?
// todo  | Code[Name[? <: T]]

/**
  * Emits to the location referred to by [[iD]], once it has a value.
  *
  * Should really be called `UnInp`. But, for convenience's sake, an exception to the naming convention is made.
  *
  * Dual of [[Inp]]
  */
object Out:
  def apply[T <: TopT](iD: OldCode[Cnst[ChanT[OffTopT, T]]]): OldCode[Xcvr[T]] = Loc(iD)
end Out
// todo  | Code[Name[? >: T]]

/**
  * General [[Polr]] for input and output. Note that it can only be an [[Expr]] or a [[Xctr]].
  */
final case class Loc[P >: BotT, N <: P & TopT](iD: OldCode[Cnst[ChanT[P, N]]]) extends OldCode[Povr[P, N]]

/**
  * A hard-coded connection between one input and one output
  */
final case class Forward[T <: TopT](inp: OldCode[Expr[T]], out: OldCode[Xctr[T]]) extends OldCode[Aplr]

/**
  * Dual of Forward. The connection between [[inp]] and [[out]] is instead achieved via variables.
  */
final case class Backward[I <: TopPoS, O <: TopPoS](
  declarations: OldCode[ArgsUS[DeclS[?]]],
  inp: OldCode[I],
  out: OldCode[O],
) extends OldCode[Bipo[I, O]]

final case class Apply[I <: TopPoS, O <: TopPoS](
  component: OldCode[Bipo[I, O]],
  input: OldCode[I],
) extends OldCode[O]

final case class Deply[I <: TopPoS, O <: TopPoS](
  component: OldCode[Bipo[I, O]],
  input: OldCode[O],
) extends OldCode[I]

/**
  * Spreads a value to multiple recipients.
  *
  * [[Spread]]`(`[[ArgsUS]]`())` is equivalent to Ignore/Sink/Forget/Discard/Drop.
  */
final case class Spread[T <: TopT](recipients: OldCode[ArgsUS[Xctr[T]]]) extends OldCode[Xcvr[T]]

/**
  * Forwards the inputted value once signalled to do so.
  */
final case class Hold[T <: TopT](signal: OldCode[Expr[PulseT]], value: OldCode[Expr[T]]) extends OldCode[Exvr[T]]

/**
  * Upon reception of a value, outputs a pulse. It only communicates the arrival of the value,
  * but forgets/ignores about the actual value.
  */
final case class Signal(trigger: OldCode[Expr[?]]) extends OldCode[Exvr[PulseT]]

/**
  * Forwards one of the inputs. Is guaranteed to forward a value if any of the inputs yields a value.
  *
  * Another way to think about this is that it forwards the value of the expression that "first" reduces to a value.
  *
  * [[Pick]]`[T] <: `[[Expr]]`[T]`
  */
type Pick[T <: TopT] = PickP[T, OffBotT]
object Pick:
  def apply[T <: TopT](inputs: OldCode[ArgsS[TopB.T, Expr[T]]]): Pick[T] = PickP(inputs)
end Pick

/**
  * Internal choice. Non-deterministically forwards the input to one of the outputs.
  *
  * [[UnPick]]`[T] <: `[[Xctr]]`[T]`
  */
type UnPick[T <: TopT] = PickP[OffTopT, T]
object UnPick:
  def apply[T <: TopT](recipients: OldCode[ArgsS[TopB.T, Xctr[T]]]): UnPick[T] = PickP(recipients)
end UnPick

/**
  * General [[Polr]] for picking. Note that it can only be an [[Expr]] or an [[Xctr]].
  */
final case class PickP[P >: BotT, N <: TopT](connections: OldCode[ArgsS[TopB.T, Polr[P, N]]]) extends OldCode[Povr[P, N]]
