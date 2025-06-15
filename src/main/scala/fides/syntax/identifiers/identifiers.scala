package fides.syntax.identifiers

import fides.syntax.core.Code
import fides.syntax.types.{Expr, IdentifierT, Ntrl}

// todo use context functions?

/**
  * Identifiers are structureless. They can only be compared for equality. They cannot be inspected in any other way.
  * New identifiers can be created. It is not possible to construct identifiers in any other way.
  *
  * Identifiers are globally unique. Their names are just a convenient representation (that may or may not be unique).
  */
class Identifier protected extends Code[Ntrl[IdentifierT]]

/**
  * Outputs a new identifier. Can be simulated with a scope with one placeholder. Added for convenience.
  */
final case class NewIdentifier() extends Code[Expr[IdentifierT]]
