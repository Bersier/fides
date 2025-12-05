package fides.syntax.identifiers

import fides.syntax.types.*

final case class Name[D <: TopD](identifier: OldCode[CnstG[IdentifierUD]]) extends OldCode[NameG[D]]
