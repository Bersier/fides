package fides.syntax.declarations

import fides.syntax.core.Code
import fides.syntax.identifiers.Name
import fides.syntax.types.{ChanT, DeclarationS, Expr, ValTop}
import izumi.reflect.Tag

// todo add type ascriptions; and maybe remove Tag

sealed trait Declaration[T <: ValTop]:
  def name: Name[T]
object Declaration:
  final case class ImmutableVariable[T <: ValTop](
    name: Name[T],
    body: Code[Expr[T]],
  ) extends Code[DeclarationS[T]]

  final case class MutableVariable[T <: ValTop : Tag](
    name: Name[T],
    body: Code[Expr[T]],
  ) extends Code[DeclarationS[T]]

  final case class FreshChannel[T <: ValTop : Tag](
    name: Name[ChanT[T, T]],
  ) extends Code[DeclarationS[ChanT[T, T]]]
end Declaration
