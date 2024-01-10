package fides2024.syntax.identifiers

import fides2024.syntax.kinds.ValType

trait LocationBuilder[I[T <: ValType] <: Identifier]:
  def apply[T <: ValType]()(using Context): I[T] = Identifier.from(constructor[T])
  def apply[T <: ValType](name: String)(using Context): I[T] = Identifier.from(constructor[T], name)
  protected def constructor[T <: ValType](name: String): I[T]
end LocationBuilder
