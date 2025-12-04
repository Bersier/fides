package fides.syntax.identifiers

import fides.syntax.types.*

final case class Name[T <: TopT](identifier: OldCode[Cnst[IdentifierUT]]) extends OldCode[NameS[T]]
