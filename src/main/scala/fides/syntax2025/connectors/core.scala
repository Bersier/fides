package fides.syntax2025.connectors

import fides.syntax2025.machinery.*

/**
  * Absorbs from the location referred to by [[iD]]. Reduces to the received val after reception.
  *
  * Dual of [[Out]]
  */
object Inp:
  def apply[D <: TopD](iD: OldCode[OldCnstG[ChanD[D, OffBotD]]]): OldCode[ExvrG[D]] = Loc(iD)
end Inp

/**
  * Emits to the location referred to by [[iD]], once it has a value.
  *
  * Should really be called `UnInp`. But, for convenience's sake, an exception to the naming convention is made.
  *
  * Dual of [[Inp]]
  */
object Out:
  def apply[D <: TopD](iD: OldCode[OldCnstG[ChanD[OffTopD, D]]]): OldCode[XcvrG[D]] = Loc(iD)
end Out

/**
  * General [[PolrG]] for input and output. Note that it can only be an [[OldExprG]] or a [[OldXctrG]].
  */
final case class Loc[P >: BotD, N <: P & TopD](iD: OldCode[OldCnstG[ChanD[P, N]]]) extends OldCode[PovrG[P, N]]

/**
  * A hard-coded connection between one input and one output
  */
final case class Forward[D <: TopD](inp: OldCode[OldExprG[D]], out: OldCode[OldXctrG[D]]) extends OldCode[AplrG]
// todo both could actually be matching polars, no? The polarities would have to be complementary.

/**
  * Dual of Forward. The connection between [[inp]] and [[out]] is instead achieved via variables.
  */
final case class Backward[I <: TopPoG, O <: TopPoG](
  declarations: OldCode[ArgsUG[DeclG[?]]],
  inp: OldCode[I],
  out: OldCode[O],
) extends OldCode[BipoG[I, O]]

final case class Apply[I <: TopPoG, O <: TopPoG](
  component: OldCode[BipoG[I, O]],
  input: OldCode[I],
) extends OldCode[O]

final case class Deply[I <: TopPoG, O <: TopPoG](
  component: OldCode[BipoG[I, O]],
  input: OldCode[O],
) extends OldCode[I]

/**
  * Spreads a value to multiple recipients.
  *
  * [[Spread]]`(`[[ArgsUG]]`())` is equivalent to Ignore/Sink/Forget/Discard/Drop.
  */
final case class Spread[D <: TopD](recipients: OldCode[ArgsUG[OldXctrG[D]]]) extends OldCode[XcvrG[D]]

/**
  * Forwards the inputted value once signalled to do so.
  */
final case class Hold[D <: TopD](signal: OldCode[OldExprG[PulseD]], value: OldCode[OldExprG[D]]) extends OldCode[ExvrG[D]]

/**
  * Upon reception of a value, outputs a pulse. It only communicates the arrival of the value,
  * but forgets/ignores about the actual value.
  */
final case class Signal(trigger: OldCode[OldExprG[?]]) extends OldCode[ExvrG[PulseD]]

/**
  * Forwards one of the inputs. Is guaranteed to forward a value if any of the inputs yields a value.
  *
  * Another way to think about this is that it forwards the value of the expression that "first" reduces to a value.
  *
  * [[Pick]]`[D] <: `[[OldExprG]]`[D]`
  */
type Pick[D <: TopD] = PickP[D, OffBotD]
object Pick:
  def apply[D <: TopD](inputs: OldCode[ArgsG[TopE.F, OldExprG[D]]]): Pick[D] = PickP(inputs)
end Pick

/**
  * Internal choice. Non-deterministically forwards the input to one of the outputs.
  *
  * [[UnPick]]`[D] <: `[[OldXctrG]]`[D]`
  */
type UnPick[D <: TopD] = PickP[OffTopD, D]
object UnPick:
  def apply[D <: TopD](recipients: OldCode[ArgsG[TopE.F, OldXctrG[D]]]): UnPick[D] = PickP(recipients)
end UnPick

/**
  * General [[PolrG]] for picking. Note that it can only be an [[OldExprG]] or an [[OldXctrG]].
  */
final case class PickP[P >: BotD, N <: TopD](
  connections: OldCode[ArgsG[TopE.F, PolrG[P, N]]],
) extends OldCode[PovrG[P, N]]
