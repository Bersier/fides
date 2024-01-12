package fides2024.syntax.identifiers

import fides2024.syntax.code.{Atom, ValQ}

import java.util.concurrent.atomic.AtomicInteger
import language.experimental.pureFunctions

// todo add way to get reflected T, maybe using izumi-reflect
// todo use context functions?

/**
  * Identifiers are structureless. They can only be compared for equality. They cannot be inspected in any other way.
  * New identifiers can be created. It is not possible to construct identifiers in any other way.
  *
  * Identifiers are globally unique. Their names are just a convenient representation (that may or may not be unique).
  *
  * Due to hacky implicit conversions using asInstanceOf,
  * we effectively have Channel[T] < Identifier and Cell[T] < Identifier.
  */
open class Identifier protected(val name: String) extends Atom, ValQ[Identifier] derives CanEqual:
  override def toString: String = s"#$name"
object Identifier:
  def apply()(using Context): Identifier = from(constructor)

  def apply(name: String)(using Context): Identifier = from(constructor, name)

  // todo make private or protect from abuse
  def from[I <: Identifier](constructor: String -> I)(using Context): I =
    from(constructor, util.symbols(currentSymbolIndex.getAndIncrement()).toString)

  // todo make private or protect from abuse
  def from[I <: Identifier](constructor: String -> I, name: String)(using Context): I =
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
