package fides.syntax.connectors

import fides.syntax.core.Code
import fides.syntax.types.{TopT, TypeS, Xctr, Xcvr}
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
