package fides.syntax.identifiers

import fides.syntax.code.{Code, CodeType, Expr, ValType, Xctr}
import fides.syntax.identifiers.naming.{Context, Named}

final class Name[T <: ValType] private(name: String) extends Named(name), CodeType, Code[Name[T]]
object Name:
  def apply[T <: ValType]()(using Context): Name[T] = Named.from(new Name(_))
  def apply[T <: ValType](name: String)(using Context): Name[T] = Named.from(new Name(_), name)
end Name

final case class VarInp[T <: ValType](name: Code[Name[T]]) extends Expr[T]

final case class VarOut[T <: ValType](name: Code[Name[T]]) extends Xctr[T]
