package fides.syntax.connectors

import fides.syntax.core.Code
import fides.syntax.types.{Aplr, Args, AtomT, CaseS, Cnst, Expr, Exvr, TopT, TypeS, Xctr, Xcvr}
import izumi.reflect.Tag

/**
  * Tries to match a value to the given pattern. Upon failure, outputs the value to the alternative instead.
  */
final case class Match[T <: TopT, U <: TopT](
  pattern: Code[Xctr[T]],
  alternative: Code[Xctr[U]] = Ignore(),
) extends Code[Xcvr[T | U]]

/**
  * Matches any value of type [[T]].
  */
final case class MatchType[T <: TopT](t: Code[TypeS[T]]) extends Code[Xcvr[T]]

/**
  * Represents a Fides type
  */
final case class Type[T <: TopT](t: Tag[T]) extends Code[TypeS[T]]

// todo Delete Switch and Route?

/**
  * Two cases may not overlap.
  */
final case class Switch[T <: TopT, A <: AtomT](
  testee : Code[Expr[A]],
  cases  : Code[Args[CaseS[T, A]]],
  default: Code[Expr[T]],
) extends Code[Exvr[T]]

final case class Case[T <: TopT, A <: AtomT](
  testValue: Code[Cnst[A]],
  expression: Code[Expr[T]],
) extends Code[CaseS[T, A]]

// todo generalize to polar?
final case class Route[A <: AtomT](
  // todo, instead of an atom, use a type partition? Isn't that what Match does?
  testee : Code[Expr[A]],
//  cases  : Code[Args[RouteCaseS[A]]],
  default: Code[Aplr],
) extends Code[Xcvr[A]]

final case class RouteCase[A <: AtomT](
  testValue: Code[Cnst[A]],
  dormant: Code[Aplr],
) // extends Code[RouteCaseS[A]]
