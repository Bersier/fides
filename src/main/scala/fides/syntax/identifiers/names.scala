package fides.syntax.identifiers

import fides.syntax.core.Code
import fides.syntax.types.{IdentifierT, Lit, NameS, Expr, ValTop}

final case class Name[T <: ValTop](identifier: Code[Lit & Expr[IdentifierT]]) extends Code[NameS[T]]
