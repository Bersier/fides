package fides.syntax.identifiers

import fides.syntax.core.Code
import fides.syntax.types.{IdentifierT, NameS, Cnst, ValTop}

final case class Name[T <: ValTop](identifier: Code[Cnst[IdentifierT]]) extends Code[NameS[T]]
