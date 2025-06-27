package fides.syntax.connectors

import fides.syntax.core.Code
import fides.syntax.types.*

/**
  * Absorbs from the location referred to by [[iD]]. Reduces to the received val after reception.
  *
  * Dual of [[Out]]
  */
object Inp:
  def apply[T <: TopT](iD: Code[Cnst[ChanT[T, OffBotT]]]): Code[Exvr[T]] = Loc(iD)
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
  def apply[T <: TopT](iD: Code[Cnst[ChanT[OffTopT, T]]]): Code[Xcvr[T]] = Loc(iD)
end Out
// todo  | Code[Name[? >: T]]

/**
  * General [[Polr]] for input and output. Note that it can only be an [[Expr]] or a [[Xctr]].
  */
final case class Loc[P >: BotT, N <: P & TopT](iD: Code[Cnst[ChanT[P, N]]]) extends Code[Povr[P, N]]

/**
  * A hard-coded connection between one input and one output
  */
final case class Forward[T <: TopT](inp: Code[Expr[T]], out: Code[Xctr[T]]) extends Code[Aplr]

/**
  * Dual of Forward. The connection between [[inp]] and [[out]] is instead achieved via variables.
  */
final case class Backward[I <: TopPoS, O <: TopPoS](
  declarations: Code[Args[DeclS[?]]],
  inp: Code[I],
  out: Code[O],
) extends Code[Bipo[I, O]]
// todo doesn't this exert control over execution?

final case class Apply[I <: TopPoS, O <: TopPoS](
  component: Code[Bipo[I, O]],
  input: Code[I],
) extends Code[O]

final case class Deply[I <: TopPoS, O <: TopPoS](
  component: Code[Bipo[I, O]],
  input: Code[O],
) extends Code[I]

/**
  * Spreads a value to multiple recipients.
  *
  * [[Spread]]`(`[[Args]]`())` is equivalent to Ignore/Sink/Forget/Discard/Drop.
  */
final case class Spread[T <: TopT](recipients: Code[Args[Xctr[T]]]) extends Code[Xcvr[T]]

/**
  * Forwards the inputted value once signalled to do so.
  */
final case class Hold[T <: TopT](signal: Code[Expr[PulseT]], value: Code[Expr[T]]) extends Code[Exvr[T]]

/**
  * Upon reception of a value, outputs a pulse. It only communicates the arrival of the value,
  * but forgets/ignores about the actual value.
  */
final case class Signal(trigger: Code[Expr[?]]) extends Code[Exvr[PulseT]]

/**
  * Forwards one of the inputs. Is guaranteed to forward a value if any of the inputs yields a value.
  *
  * Another way to think about this is that it forwards the value of the expression that "first" reduces to a value.
  *
  * [[Pick]]`[T] <: `[[Expr]]`[T]`
  */
type Pick[T <: TopT] = PickP[T, OffBotT]
object Pick:
  def apply[T <: TopT](inputs: Code[ArgsS[true, Expr[T]]]): Pick[T] = PickP(inputs)

/**
  * Internal choice. Non-deterministically forwards the input to one of the outputs.
  *
  * [[UnPick]]`[T] <: `[[Xctr]]`[T]`
  */
type UnPick[T <: TopT] = PickP[OffTopT, T]
object UnPick:
  def apply[T <: TopT](recipients: Code[ArgsS[true, Xctr[T]]]): UnPick[T] = PickP(recipients)
end UnPick

/**
  * General [[Polr]] for picking. Note that it can only be an [[Expr]] or an [[Xctr]].
  */
final case class PickP[P >: BotT, N <: TopT](connections: Code[ArgsS[true, Polr[P, N]]]) extends Code[Povr[P, N]]
