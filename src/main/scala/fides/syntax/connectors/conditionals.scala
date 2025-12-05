package fides.syntax.connectors

import fides.syntax.types.*
import izumi.reflect.Tag

/**
  * Tries to match a value to the given pattern. Upon failure, outputs the value to the alternative instead.
  */
final case class Match[D1 <: TopD, D2 <: TopD](
  pattern: OldCode[Xctr[D1]],
  alternative: OldCode[Xctr[D2]],
) extends OldCode[Xcvr[D1 | D2]]

/**
  * Matches any value of type [[D]].
  */
final case class MatchType[D <: TopD](t: OldCode[TypeS[D]]) extends OldCode[Xcvr[D]]

/**
  * Represents a Fides type
  */
final case class Type[D <: TopD](t: Tag[D]) extends OldCode[TypeS[D]]
