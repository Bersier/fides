package fides.syntax.identifiers

import fides.syntax.core.Code
import fides.syntax.types.{Cnst, IdentifierUT, NameS, TopT}

final case class Name[T <: TopT](identifier: Code[Cnst[IdentifierUT]]) extends Code[NameS[T]]
