package fides.syntax.declarations

import fides.syntax.core.Code
import fides.syntax.types.{CodeType, Expr, ValTop}
import fides.syntax.identifiers.{Channel, Name}
import izumi.reflect.Tag

// todo add type ascriptions; and maybe remove Tag

sealed trait Declaration[T <: ValTop] extends CodeType:
  def name: Name[T]
object Declaration:
  final case class ImmutableVariable[T <: ValTop](
    name: Name[T],
    body: Expr[T],
  ) extends Declaration[T], Code[Declaration[T]]

  final case class MutableVariable[T <: ValTop : Tag](
    name: Name[T],
    body: Expr[T],
  ) extends Declaration[T], Code[Declaration[T]]

  final case class FreshChannel[T <: ValTop : Tag](
    name: Name[Channel[T]],
  ) extends Declaration[Channel[T]], Code[Declaration[Channel[T]]]
end Declaration
