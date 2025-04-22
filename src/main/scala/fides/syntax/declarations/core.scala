package fides.syntax.declarations

import fides.syntax.code.{Code, CodeType, Expr, ValType}
import fides.syntax.identifiers.{Channel, Name}
import izumi.reflect.Tag

// todo add type ascriptions; and maybe remove Tag

sealed trait Declaration[T <: ValType] extends CodeType:
  def name: Name[T]
object Declaration:
  final case class ImmutableVariable[T <: ValType](
    name: Name[T],
    body: Expr[T],
  ) extends Declaration[T], Code[Declaration[T]]

  final case class MutableVariable[T <: ValType : Tag](
    name: Name[T],
    body: Expr[T],
  ) extends Declaration[T], Code[Declaration[T]]

  final case class FreshChannel[T <: ValType : Tag](
    name: Name[Channel[T]],
  ) extends Declaration[Channel[T]], Code[Declaration[Channel[T]]]
end Declaration
