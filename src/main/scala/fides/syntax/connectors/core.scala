package fides.syntax.connectors

import fides.syntax.core.Code
import fides.syntax.types.{Args, ArgsS, BiPo, ChanT, Cnst, DeclarationS, Expr, OffBot, OffTop, PoTop, Polar, Process, PulseT, ValBot, ValTop, Xctr}

/**
  * Absorbs from the location referred to by [[iD]]. Reduces to the received val after reception.
  *
  * Dual of [[Out]]
  */
object Inp:
  def apply[T <: ValTop](iD: Code[Cnst[ChanT[T, OffBot]]]): Code[Expr[T]] = Loc(iD)
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
  def apply[T <: ValTop](iD: Code[Cnst[ChanT[OffTop, T]]]): Code[Xctr[T]] = Loc(iD)
end Out
// todo  | Code[Name[? >: T]]

/**
  * General [[Polar]] for input and output. Note that it can only be an [[Expr]] or a [[Xctr]].
  */
final case class Loc[P >: ValBot, N <: ValTop](iD: Code[Cnst[ChanT[P, N]]]) extends Code[Polar[P, N]]

/**
  * A hard-coded connection between one input and one output
  *
  * Equivalent to [[Spread]]([[inp]], [[Args]]([[out]])).
  */
final case class Forward[T <: ValTop](inp: Code[Expr[T]], out: Code[Xctr[T]]) extends Code[Process]

/**
  * Dual of Forward. The connection between [[inp]] and [[out]] is instead achieved via variables.
  *
  * Equivalent to [[Spread]]([[inp]], [[Args]]([[out]])).
  */
final case class Backward[I <: PoTop, O <: PoTop](
  declarations: Code[Args[DeclarationS[?]]],
  inp: Code[I],
  out: Code[O],
) extends Code[BiPo[I, O]]

final case class Apply[I <: PoTop, O <: PoTop](
  component: Code[BiPo[I, O]],
  input: Code[I],
) extends Code[O]

final case class Deply[I <: PoTop, O <: PoTop](
  component: Code[BiPo[I, O]],
  input: Code[O],
) extends Code[I]

/**
  * Kind-of the dual of values.
  *
  * Aka Sink, Forget
  *
  * Can be implemented in terms of the other primitives.
  */
final case class Ignore() extends Code[Xctr[ValTop]]

/**
  * Spreads a value to multiple recipients.
  */
final case class Spread[T <: ValTop](recipients: Code[Args[Xctr[T]]]) extends Code[Xctr[T]]

/**
  * Forwards the inputted value once signalled to do so.
  */
final case class Hold[T <: ValTop](signal: Code[Expr[PulseT]], value: Code[Expr[T]]) extends Code[Expr[T]]

/**
  * Upon reception of a value, outputs a pulse. It only communicates the arrival of the value,
  * but forgets/ignores about the actual value.
  */
final case class Signal(trigger: Code[Expr[?]]) extends Code[Expr[PulseT]]

/**
  * Forwards one of the inputs. Is guaranteed to forward a value if any of the inputs yields a value.
  *
  * Another way to think about this is that it forwards the value of the expression that "first" reduces to a value.
  *
  * [[Pick]]`[T] <: `[[Expr]]`[T]`
  */
type Pick[T <: ValTop] = PickP[T, OffBot]
object Pick:
  def apply[T <: ValTop](inputs: Code[ArgsS[true, Expr[T]]]): Pick[T] = PickP(inputs)

/**
  * Internal choice. Non-deterministically forwards the input to one of the outputs.
  *
  * [[UnPick]]`[T] <: `[[Xctr]]`[T]`
  */
type UnPick[T <: ValTop] = PickP[OffTop, T]
object UnPick:
  def apply[T <: ValTop](recipients: Code[ArgsS[true, Xctr[T]]]): UnPick[T] = PickP(recipients)
end UnPick

/**
  * General [[Polar]] for picking. Note that it can only be an [[Expr]] or an [[Xctr]].
  */
final case class PickP[P >: ValBot, N <: ValTop](connections: Code[ArgsS[true, Polar[P, N]]]) extends Code[Polar[P, N]]
