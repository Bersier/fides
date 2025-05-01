package fides.syntax.connectors

import fides.syntax.core.Code
import fides.syntax.meta.Args
import fides.syntax.types.{Args, ArgsS, ChanT, DeclarationS, Expr, Lit, OffBot, OffTop, Polar, Process, PulseT, ValBot, ValTop, Xctr}

/**
  * Absorbs from the location referred to by [[iD]]. Reduces to the received val after reception.
  *
  * Dual of [[Out]]
  */
object Inp:
  def apply[T <: ValTop](iD: Code[Lit & Expr[ChanT[T, OffBot]]]): Code[Polar[T, OffBot]] = Loc(iD)
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
  def apply[T <: ValTop](iD: Code[Expr[ChanT[OffTop, T]] & Lit]): Code[Polar[OffTop, T]] = Loc(iD)
end Out
// todo  | Code[Name[? >: T]]

/**
  * General [[Polar]] for input and output. Note that it can only be an [[Expr]] or a [[Xctr]].
  */
final case class Loc[P >: ValBot, N <: ValTop](iD: Code[Lit & Expr[ChanT[P, N]]]) extends Code[Polar[P, N]]
// todo  | Code[Name[? >: Nothing <: ValTop]]

// todo given the constraint  (R =:= Positive) | ((R =:= Negative) &:& (P =:= Nothing)),
//  can this even be used in a polymorphic abstraction where the polarity is not known in advance?
//  Relatedly, we should be careful about not unintentionally leaking certain features of the Scala type system into
//  Fides. If the polymorphic abstraction can only be used with an implicit proof that the type parameter satisfies
//  certain properties, then it is pointless for Fides. Similarly, we don't want to introduce the '?' type wildcard
//  into Fides, do we?

// TODO But why did we want to make this generic in the first place? What's the issue with having separate constructs
//  for separate polarities? I forgot... I think it leads to redundancy somewhere, but where? Don't we want the syntax
//  to make the polarity explicit, rather than in just the composition rules? It's probably something to do with
//  metaprogramming...


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
final case class Backward[T <: ValTop, U <: ValTop](
  declarations: Code[Args[DeclarationS[?]]],
  inp: Code[Xctr[T]],
  out: Code[Expr[U]],
) extends Code[Polar[T, U]]

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
