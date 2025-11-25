package fides.syntax.identifiers

import fides.syntax.core.Code
import fides.syntax.types.{Cnst, IdentifierIT, IdentifierT, NameS, TopT}

final case class Name[T <: TopT](identifier: Code[Cnst[IdentifierIT]]) extends Code[NameS[T]]
