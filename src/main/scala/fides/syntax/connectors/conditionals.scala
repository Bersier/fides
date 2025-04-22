package fides.syntax.connectors

import fides.syntax.code.{Atom, Code, CodeType, Expr, Ntrl, Process, Lit, ValTop, Xctr}
import fides.syntax.meta.Args
import fides.syntax.values.{Collected, CollectedG}
import izumi.reflect.Tag

/**
  * Tries to match a value to the given pattern. Upon failure, outputs the value to the alternative instead.
  *
  * Unfortunately, it doesn't seem possible to refine the type of [[alternative]] in Scala.
  */
final case class Match[T <: ValTop, U <: ValTop](
  pattern: Code[Xctr[T]],
  alternative: Code[Xctr[U]] = Ignore(),
) extends Xctr[T | U]

/**
  * Matches any value of type [[T]].
  */
final case class MatchType[T <: ValTop](t: Code[Type[T]]) extends Xctr[T]

/**
  * Represents a Fides type
  */
final case class Type[T <: ValTop](t: Tag[T]) extends CodeType, Code[Type[T]]

final case class Switch[T <: ValTop, A <: Atom](
  input  : Code[Expr[T]],
  testee : Code[Expr[A]],
  cases  : Code[Args[Case[T, A]]],
  default: Code[Xctr[T]] = Ignore(),
) extends Process

final case class Case[T <: ValTop, A <: Atom](
  testValue: Code[Ntrl[A] & Lit],
  extractor: Code[Xctr[T]],
) extends Code[Case[T, A]], CodeType
