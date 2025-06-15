package fides.syntax.connectors

import fides.syntax.core.Code
import fides.syntax.types.{Args, AtomT, CaseS, Cnst, Expr, TopT, TypeS, Xctr}
import izumi.reflect.Tag

/**
  * Tries to match a value to the given pattern. Upon failure, outputs the value to the alternative instead.
  */
final case class Match[T <: TopT, U <: TopT](
  pattern: Code[Xctr[T]],
  alternative: Code[Xctr[U]] = Ignore(),
) extends Code[Xctr[T | U]]

/**
  * Matches any value of type [[T]].
  */
final case class MatchType[T <: TopT](t: Code[TypeS[T]]) extends Code[Xctr[T]]

/**
  * Represents a Fides type
  */
final case class Type[T <: TopT](t: Tag[T]) extends Code[TypeS[T]]

/**
  * Two cases may not overlap.
  */
final case class Switch[T <: TopT, A <: AtomT](
  testee : Code[Expr[A]],
  cases  : Code[Args[CaseS[T, A]]],
  default: Code[Expr[T]],
) extends Code[Expr[T]]

final case class Case[T <: TopT, A <: AtomT](
  testValue: Code[Cnst[A]],
  extractor: Code[Expr[T]],
) extends Code[CaseS[T, A]]
