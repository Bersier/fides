package fides.syntax.identifiers

import fides.syntax.types.*

final case class Name[D <: TopD](identifier: OldCode[Cnst[IdentifierUD]]) extends OldCode[NameS[D]]
