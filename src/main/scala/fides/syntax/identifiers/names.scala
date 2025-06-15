package fides.syntax.identifiers

import fides.syntax.core.Code
import fides.syntax.types.{Cnst, IdentifierT, NameS, TopT}

final case class Name[T <: TopT](identifier: Code[Cnst[IdentifierT]]) extends Code[NameS[T]]
