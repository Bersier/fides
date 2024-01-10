package fides2024.syntax.meta

import fides2024.syntax.identifiers.Identifier
import fides2024.syntax.kinds.{Code, CodeType, Expr}

/**
  * Applies a substitution to a Quoted.
  *
  * @param original    the identifier to be replaced/renamed
  * @param replacement the identifer that is to replace the original
  * @param code        the Quoted in which the replacement is to take place
  */
final case class Substitute[C <: CodeType](
  original   : Code[Expr[Identifier]],
  replacement: Code[Expr[Identifier]],
  code       : Code[Expr[Quoted[C]]],
) extends Expr[Quoted[C]]
