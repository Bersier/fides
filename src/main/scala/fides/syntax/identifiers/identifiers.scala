package fides.syntax.identifiers

import fides.syntax.code.{Atom, Expr, Val}
import fides.syntax.identifiers.naming.{Context, Named}

// todo use context functions?

/**
  * Identifiers are structureless. They can only be compared for equality. They cannot be inspected in any other way.
  * New identifiers can be created. It is not possible to construct identifiers in any other way.
  *
  * Identifiers are globally unique. Their names are just a convenient representation (that may or may not be unique).
  */
open class Identifier protected(
  protected[identifiers] val name: String,
) extends Atom, Val[Identifier], Named derives CanEqual:
  override def toString: String = s"#$name"
object Identifier:
  def apply()(using Context): Identifier = Named.from(new Identifier(_))
  def apply(name: String)(using Context): Identifier = Named.from(new Identifier(_), name)
end Identifier

/**
  * Outputs a new identifier. Can be simulated with a scope with one placeholder. Added for convenience.
  */
final case class NewIdentifier() extends Expr[Identifier]
