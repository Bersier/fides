package fides.syntax.identifiers

import fides.syntax.types.*

final case class Name[D <: TopD](identifier: OldCode[CnstS[IdentifierUD]]) extends OldCode[NameS[D]]
