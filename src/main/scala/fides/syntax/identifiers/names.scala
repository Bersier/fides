package fides.syntax.identifiers

import fides.syntax.core.Code
import fides.syntax.types.{IdentifierT, Lit, NameS, Ntrl, ValTop}

final case class Name[T <: ValTop](identifier: Code[Lit & Ntrl[IdentifierT]]) extends Code[NameS[T]]
