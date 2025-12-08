package fides.syntax.identifiers

import fides.syntax.machinery.*

final case class Name[D <: TopD](identifier: OldCode[OldCnstG[IdentifierUD]]) extends OldCode[OldNameG[D]]
