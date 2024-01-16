package fides.syntax.identifiers

import fides.syntax.code.{Atom, Expr, ValQ}

import java.util.concurrent.atomic.AtomicInteger
import language.experimental.pureFunctions

// todo use context functions?

/**
  * Identifiers are structureless. They can only be compared for equality. They cannot be inspected in any other way.
  * New identifiers can be created. It is not possible to construct identifiers in any other way.
  *
  * Identifiers are globally unique. Their names are just a convenient representation (that may or may not be unique).
  */
open class Identifier private[identifiers](val name: String) extends Atom, ValQ[Identifier] derives CanEqual:
  override def toString: String = s"#$name"
object Identifier:
  def apply()(using Context): Identifier = from(constructor)

  def apply(name: String)(using Context): Identifier = from(constructor, name)

  private[identifiers] def from[I <: Identifier](constructor: String -> I)(using Context): I =
    from(constructor, util.symbols(currentSymbolIndex.getAndIncrement()).toString)

  private[identifiers] def from[I <: Identifier](constructor: String -> I, name: String)(using Context): I =
    val identifier = constructor(qualifiedName(name))
    val previous   = summon[Context].names.putIfAbsent(name, identifier)
    require(previous.isEmpty, s"$name is already used for ${previous.toString}_${previous.get.hashCode().toHexString}")
    identifier

  private def qualifiedName(name: String)(using Context): String =
    val globalPrefix = Some(summon[Context].prefix).filter(_.nonEmpty).map(_ + ".").getOrElse("")
    s"$globalPrefix$name"

  private val currentSymbolIndex = AtomicInteger(0)
  private val constructor: String -> Identifier = new Identifier(_)
end Identifier
// todo have more specific identifier types? Or user-defined ones?

/**
  * Outputs a new identifier. Can be simulated with a scope with one placeholder. Added for convenience.
  */
final case class NewIdentifier() extends Expr[Identifier]
