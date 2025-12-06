package fides.syntax.connectors

import fides.syntax.machinery.*
import izumi.reflect.Tag

/**
  * Tries to match a value to the given pattern. Upon failure, outputs the value to the alternative instead.
  */
final case class Match[D1 <: TopD, D2 <: TopD](
  pattern: OldCode[OldXctrG[D1]],
  alternative: OldCode[OldXctrG[D2]],
) extends OldCode[XcvrG[D1 | D2]]

/**
  * Matches any value of type [[D]].
  */
final case class MatchType[D <: TopD](t: OldCode[TypeG[D]]) extends OldCode[XcvrG[D]]

/**
  * Represents a Fides type
  */
final case class Type[D <: TopD](t: Tag[D]) extends OldCode[TypeG[D]]
