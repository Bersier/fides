package fides.syntax.identifiers

import fides.syntax.machinery.*

final case class Name[D <: TopD](identifier: OldCode[CnstG[IdentifierUD]]) extends OldCode[NameG[D]]
