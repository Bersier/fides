package fides2024.syntax.values

import fides2024.syntax.*

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.concurrent

// todo add way to get reflected T, maybe using izumi-reflect
// todo use context functions?

// todo
trait Context:
  def prefix: String
  val names: concurrent.Map[String, Identifier] // todo garbage-collect?
end Context

/**
  * Identifiers are structureless. They can only be compared for equality. They cannot be inspected in any other way.
  * New identifiers can be created. It is not possible to construct identifiers in any other way.
  *
  * Identifiers are globally unique. Their names are just a convenient representation (that may or may not be unique).
  *
  * Due to hacky implicit conversions using asInstanceOf,
  * we effectively have Channel[T] < Identifier and Cell[T] < Identifier.
  */
open class Identifier protected(val name: String) extends Val[Identifier] derives CanEqual:
  override def toString: String = s"I_$name"
object Identifier:
  def apply()(using Context): Identifier = from(constructor)

  def apply(name: String)(using Context): Identifier = from(constructor, name)

  // todo make private or protect from abuse
  def from[I <: Identifier](constructor: String => I)(using Context): I =
    from(constructor, util.symbols(currentSymbolIndex.getAndIncrement()).toString)

  // todo make private or protect from abuse
  def from[I <: Identifier](constructor: String => I, name: String)(using Context): I =
    val identifier = constructor(qualifiedName(name))
    val previous   = summon[Context].names.putIfAbsent(name, identifier)
    require(previous.isEmpty, s"$name is already used for ${previous.toString}_${previous.get.hashCode().toHexString}")
    identifier

  private def qualifiedName(name: String)(using Context): String =
    val globalPrefix = Some(summon[Context].prefix).filter(_.nonEmpty).map(_ + ".").getOrElse("")
    s"$globalPrefix$name"

  private val currentSymbolIndex = AtomicInteger(0)
  private val constructor: String => Identifier = new Identifier(_)
end Identifier

trait LocationBuilder[I[T <: ValType] <: Identifier]:
  def apply[T <: ValType]()(using Context): I[T] = Identifier.from(constructor[T])
  def apply[T <: ValType](name: String)(using Context): I[T] = Identifier.from(constructor[T], name)
  def constructor[T <: ValType](name: String): I[T]
end LocationBuilder

/**
  * A type of location used for channels
  *
  * @tparam T the type of the values that transit through the channel
  */
open class Channel[T <: ValType] protected(name: String) extends Identifier(name), Val[Channel[T]]:
  override def toString: String = s"C_$name"
object Channel extends LocationBuilder[Channel]:
  def constructor[T <: ValType](name: String) = new Channel(name)
end Channel

/**
  * A type of location used for memory cells
  *
  * A cell guarantees that there is a way to temporally order operations made on it
  * in a way that is consistent with observations.
  *
  * @tparam T the type of the values that get stored in the cell
  */
open class Cell[T <: ValType] protected(name: String) extends Identifier(name), Val[Cell[T]]:
  override def toString: String = s"M_$name"
object Cell extends LocationBuilder[Cell]:
  def constructor[T <: ValType](name: String) = new Cell(name)
end Cell
// todo Does it need an initial value? I think so.

/**
  * A key has a corresponding identifier. The identifer can be obtained from the key, but not vice versa
  * (there might not even exist a corresponding key for some identifiers).
  */
sealed class IdentifierKey(using Context) extends Val[IdentifierKey]:
  val identifier: Identifier = Identifier()
  override def toString: String = s"Key(${identifier.name})"
end IdentifierKey

final class ChannelKey[T <: ValType](using Context) extends IdentifierKey:
  override val identifier: Channel[T] = Channel()
end ChannelKey
