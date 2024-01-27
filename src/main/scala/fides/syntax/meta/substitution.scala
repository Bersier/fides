package fides.syntax.meta

import fides.syntax.code.{Code, CodeType, Expr}
import fides.syntax.identifiers.Identifier

/**
  * Applies a substitution to a Quoted.
  *
  * @param original    the identifier to be replaced/renamed
  * @param replacement the identifer that is to replace the original
  * @param code        the Quoted in which the replacement is to take place
  */
final case class Substitute[S <: CodeType](
  original   : Code[Expr[Identifier]],
  replacement: Code[Expr[Identifier]],
  code       : Code[Expr[Quoted[S]]],
) extends Expr[Quoted[S]]
