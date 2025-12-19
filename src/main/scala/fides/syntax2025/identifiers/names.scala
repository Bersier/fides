package fides.syntax2025.identifiers

import fides.syntax2025.machinery.*

final case class Name[D <: TopD](identifier: OldCode[OldCnstG[IdentifierUD]]) extends OldCode[OldNameG[D]]
