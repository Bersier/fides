package fides.syntax.identifiers

import fides.syntax.types.*

final case class Name[T <: TopT](identifier: Code[Cnst[IdentifierUT]]) extends Code[NameS[T]]
