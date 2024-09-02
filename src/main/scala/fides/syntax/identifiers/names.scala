package fides.syntax.identifiers

import fides.syntax.code.{Code, CodeType, ValType}
import fides.syntax.identifiers.naming.{Context, Named}

final class Name[T <: ValType] private(protected[identifiers] val name: String) extends Named, CodeType, Code[Name[T]]
object Name:
  def apply[T <: ValType]()(using Context): Name[T] = Named.from(new Name(_))
  def apply[T <: ValType](name: String)(using Context): Name[T] = Named.from(new Name(_), name)
end Name
