package fides.syntax.connectors

import fides.syntax.types.*

/**
  * Absorbs from the location referred to by [[iD]]. Reduces to the received val after reception.
  *
  * Dual of [[Out]]
  */
object Inp:
  def apply[D <: TopD](iD: OldCode[CnstS[ChanD[D, OffBotD]]]): OldCode[ExvrS[D]] = Loc(iD)
end Inp
// todo add variance, like here, to all primitives, for the sake of metaprogramming?
// todo  | Code[Name[? <: D]]

/**
  * Emits to the location referred to by [[iD]], once it has a value.
  *
  * Should really be called `UnInp`. But, for convenience's sake, an exception to the naming convention is made.
  *
  * Dual of [[Inp]]
  */
object Out:
  def apply[D <: TopD](iD: OldCode[CnstS[ChanD[OffTopD, D]]]): OldCode[XcvrS[D]] = Loc(iD)
end Out
// todo  | Code[Name[? >: D]]

/**
  * General [[PolrS]] for input and output. Note that it can only be an [[ExprS]] or a [[XctrS]].
  */
final case class Loc[P >: BotD, N <: P & TopD](iD: OldCode[CnstS[ChanD[P, N]]]) extends OldCode[PovrS[P, N]]

/**
  * A hard-coded connection between one input and one output
  */
final case class Forward[D <: TopD](inp: OldCode[ExprS[D]], out: OldCode[XctrS[D]]) extends OldCode[AplrS]

/**
  * Dual of Forward. The connection between [[inp]] and [[out]] is instead achieved via variables.
  */
final case class Backward[I <: TopPoS, O <: TopPoS](
  declarations: OldCode[ArgsUS[DeclS[?]]],
  inp: OldCode[I],
  out: OldCode[O],
) extends OldCode[BipoS[I, O]]

final case class Apply[I <: TopPoS, O <: TopPoS](
  component: OldCode[BipoS[I, O]],
  input: OldCode[I],
) extends OldCode[O]

final case class Deply[I <: TopPoS, O <: TopPoS](
  component: OldCode[BipoS[I, O]],
  input: OldCode[O],
) extends OldCode[I]

/**
  * Spreads a value to multiple recipients.
  *
  * [[Spread]]`(`[[ArgsUS]]`())` is equivalent to Ignore/Sink/Forget/Discard/Drop.
  */
final case class Spread[D <: TopD](recipients: OldCode[ArgsUS[XctrS[D]]]) extends OldCode[XcvrS[D]]

/**
  * Forwards the inputted value once signalled to do so.
  */
final case class Hold[D <: TopD](signal: OldCode[ExprS[PulseD]], value: OldCode[ExprS[D]]) extends OldCode[ExvrS[D]]

/**
  * Upon reception of a value, outputs a pulse. It only communicates the arrival of the value,
  * but forgets/ignores about the actual value.
  */
final case class Signal(trigger: OldCode[ExprS[?]]) extends OldCode[ExvrS[PulseD]]

/**
  * Forwards one of the inputs. Is guaranteed to forward a value if any of the inputs yields a value.
  *
  * Another way to think about this is that it forwards the value of the expression that "first" reduces to a value.
  *
  * [[Pick]]`[D] <: `[[ExprS]]`[D]`
  */
type Pick[D <: TopD] = PickP[D, OffBotD]
object Pick:
  def apply[D <: TopD](inputs: OldCode[ArgsS[Empty.F, ExprS[D]]]): Pick[D] = PickP(inputs)
end Pick

/**
  * Internal choice. Non-deterministically forwards the input to one of the outputs.
  *
  * [[UnPick]]`[D] <: `[[XctrS]]`[D]`
  */
type UnPick[D <: TopD] = PickP[OffTopD, D]
object UnPick:
  def apply[D <: TopD](recipients: OldCode[ArgsS[Empty.F, XctrS[D]]]): UnPick[D] = PickP(recipients)
end UnPick

/**
  * General [[PolrS]] for picking. Note that it can only be an [[ExprS]] or an [[XctrS]].
  */
final case class PickP[P >: BotD, N <: TopD](
  connections: OldCode[ArgsS[Empty.F, PolrS[P, N]]],
) extends OldCode[PovrS[P, N]]
