package fides.syntax.identifiers

import fides.syntax.core.Code
import fides.syntax.types.{Exvr, IdentifierT, Ntrl}
import util.ID

// todo use context functions?

/**
  * Identifiers are structureless. They can only be compared for equality. They cannot be inspected in any other way.
  * New identifiers can be created. Identifiers are globally unique.
  */
final case class Identifier[K <: ID](private val k: K) extends Code[Ntrl[IdentifierT[K]]]

/**
  * Outputs a new identifier. Can be simulated with a scope with one placeholder. Added for convenience.
  */
final case class NewIdentifier() extends Code[Exvr[IdentifierT[ID]]]
// todo NewKey?
