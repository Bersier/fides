package fides.syntax.connectors

import fides.syntax.core.Code
import fides.syntax.types.{Args, AtomT, CaseS, Expr, Lit, Ntrl, Process, TypeS, ValTop, Xctr}
import izumi.reflect.Tag

/**
  * Tries to match a value to the given pattern. Upon failure, outputs the value to the alternative instead.
  */
final case class Match[T <: ValTop, U <: ValTop](
  pattern: Code[Xctr[T]],
  alternative: Code[Xctr[U]] = Ignore(),
) extends Code[Xctr[T | U]]

/**
  * Matches any value of type [[T]].
  */
final case class MatchType[T <: ValTop](t: Code[TypeS[T]]) extends Code[Xctr[T]]

/**
  * Represents a Fides type
  */
final case class Type[T <: ValTop](t: Tag[T]) extends Code[TypeS[T]]

/**
  * Two cases may not overlap.
  */
final case class Switch[T <: ValTop, A <: AtomT](
  testee : Code[Expr[A]],
  cases  : Code[Args[CaseS[T, A]]],
  default: Code[Expr[T]],
) extends Code[Expr[T]]

final case class Case[T <: ValTop, A <: AtomT](
  testValue: Code[Expr[A] & Lit],
  extractor: Code[Expr[T]],
) extends Code[CaseS[T, A]]
