package fides.syntax.identifiers

import fides.syntax.core.Code
import fides.syntax.types.{Cnst, IdentifierT, NameS, TopT}
import util.ID

final case class Name[T <: TopT](identifier: Code[Cnst[IdentifierT[ID]]]) extends Code[NameS[T]]
