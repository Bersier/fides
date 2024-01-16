package fides.syntax.identifiers

import fides.syntax.code.ValType

// todo delete
trait LocationBuilder[I[T <: ValType] <: Identifier]:
  def apply[T <: ValType]()(using Context): I[T] = Identifier.from(constructor[T])
  def apply[T <: ValType](name: String)(using Context): I[T] = Identifier.from(constructor[T], name)
  protected def constructor[T <: ValType](name: String): I[T]
end LocationBuilder

// todo Cell and Channel could be subtypes of Location
