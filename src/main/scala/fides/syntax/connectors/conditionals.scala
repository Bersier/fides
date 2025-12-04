package fides.syntax.connectors

import fides.syntax.types.*
import izumi.reflect.Tag

/**
  * Tries to match a value to the given pattern. Upon failure, outputs the value to the alternative instead.
  */
final case class Match[T <: TopT, U <: TopT](
  pattern: OldCode[Xctr[T]],
  alternative: OldCode[Xctr[U]],
) extends OldCode[Xcvr[T | U]]

/**
  * Matches any value of type [[T]].
  */
final case class MatchType[T <: TopT](t: OldCode[TypeS[T]]) extends OldCode[Xcvr[T]]

/**
  * Represents a Fides type
  */
final case class Type[T <: TopT](t: Tag[T]) extends OldCode[TypeS[T]]
