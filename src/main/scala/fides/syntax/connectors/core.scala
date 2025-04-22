package fides.syntax.connectors

import fides.syntax.code.{Code, Expr, Lit, Polar, Process, ValTop, Xctr}
import fides.syntax.declarations.Declaration
import fides.syntax.identifiers.{Chan, InpChan, Name, OutChan}
import fides.syntax.meta.Args
import fides.syntax.values.Pulse
import util.&:&

/**
  * Absorbs from the location referred to by [[iD]]. Reduces to the received val after reception.
  *
  * Dual of [[Out]]
  */
final case class Inp[+T <: ValTop](iD: Code[Expr[InpChan[T]] & Lit]) extends Expr[T]
// todo add variance, like here, to all primitives, for the sake of metaprogramming?
// todo  | Code[Name[? <: T]]

/**
  * Emits to the location referred to by [[iD]], once it has a value.
  *
  * Should really be called `UnInp`. But, for convenience's sake, an exception to the naming convention is made.
  *
  * Dual of [[Inp]]
  */
final case class Out[-T <: ValTop](iD: Code[Expr[OutChan[T]] & Lit]) extends Xctr[T]
// todo these should become type aliases, if LocP is to make any sense.
// todo  | Code[Name[? >: T]]

/**
  * General [[Polar]] for input and output. Note that it can only be an [[Expr]] or a [[Xctr]].
  */
final case class Loc[+P >: Nothing, -N <: ValTop](
  iD: Code[Lit[Chan[P, N]]] | Code[Name[? >: Nothing <: ValTop]],
) extends Polar[P, N]
// todo Code[Name[? >: Nothing <: ValTop]]

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
final case class Forward[T <: ValTop](inp: Code[Expr[T]], out: Code[Xctr[T]]) extends Process

/**
  * Dual of Forward. The connection between [[inp]] and [[out]] is instead achieved via variables.
  *
  * Equivalent to [[Spread]]([[inp]], [[Args]]([[out]])).
  */
final case class Backward[T <: ValTop, U <: ValTop](
  declarations: Code[Args[Declaration[?]]],
  inp: Code[Xctr[T]],
  out: Code[Expr[U]],
) extends Polar[Polarity.Neutral, T, U]

/**
  * Kind-of the dual of values.
  *
  * Aka Sink, Forget
  *
  * Can be implemented in terms of the other primitives.
  */
final case class Ignore() extends Xctr[ValTop]

/**
  * Spreads a value to multiple recipients.
  */
final case class Spread[T <: ValTop](recipients: Code[Args[Xctr[T]]]) extends Xctr[T]

/**
  * Forwards the inputted value once signalled to do so.
  */
final case class Hold[T <: ValTop](signal: Code[Expr[Pulse]], value: Code[Expr[T]]) extends Expr[T]

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
type Pick[T <: ValTop] = PickP[Positive, T, ValTop]
object Pick:
  inline def apply[T <: ValTop](inline inputs: Code[Args.Some[Expr[T]]]): Pick[T] = PickP(inputs)

/**
  * Internal choice. Non-deterministically forwards the input to one of the outputs.
  *
  * [[UnPick]]`[T] <: `[[Xctr]]`[T]`
  */
type UnPick[T <: ValTop] = PickP[Negative, Nothing, T]
object UnPick:
  inline def apply[T <: ValTop](inline recipients: Code[Args.Some[Xctr[T]]]): UnPick[T] = PickP(recipients)
end UnPick

/**
  * General [[Polar]] for picking. Note that it can only be an [[Expr]] or an [[Xctr]].
  */
final case class PickP[R >: Positive & Negative <: Polarity, P <: N, N <: ValTop](
  connections: Code[Args.Some[Polar[R, P, N]]],
)(using (R =:= Positive) | ((R =:= Negative) &:& (P =:= Nothing))) extends Polar[R, P, N]
