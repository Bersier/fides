package fides.syntax.declarations

import fides.syntax.code.{Code, CodeType, Expr, ValType}
import fides.syntax.identifiers.{Channel, Name}

sealed trait Declaration[T <: ValType](val name: Name[T]) extends CodeType
object Declaration:
  final case class ImmutableVariable[T <: ValType](
    name: Name[T],
    body: Expr[T],
  ) extends Declaration(name), Code[Declaration[T]]
  
  final case class MutableVariable[T <: ValType](
    name: Name[T],
    body: Expr[T],
  ) extends Declaration(name), Code[Declaration[T]]
  
  final case class FreshChannel[T <: ValType](
    name: Name[Channel[T]],
  ) extends Declaration(name), Code[Declaration[Channel[T]]]
end Declaration
