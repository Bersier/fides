package fides.syntax.connectors

import fides.syntax.core.Code
import fides.syntax.types.{Args, AtomT, CaseS, Expr, Lit, Ntrl, Process, TypeS, ValTop, Xctr}
import izumi.reflect.Tag

/**
  * Tries to match a value to the given pattern. Upon failure, outputs the value to the alternative instead.
  *
  * Unfortunately, it doesn't seem possible to refine the type of [[alternative]] in Scala.
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

final case class Switch[T <: ValTop, A <: AtomT](
  input  : Code[Expr[T]],
  testee : Code[Expr[A]],
  cases  : Code[Args[CaseS[T, A]]],
  default: Code[Xctr[T]] = Ignore(),
) extends Code[Process]

final case class Case[T <: ValTop, A <: AtomT](
  testValue: Code[Ntrl[A] & Lit],
  extractor: Code[Xctr[T]],
) extends Code[CaseS[T, A]]
