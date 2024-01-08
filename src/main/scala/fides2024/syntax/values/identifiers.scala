package fides2024.syntax.values

import fides2024.syntax.*

import java.util.concurrent.atomic.AtomicLong

// todo try using type classes instead of subtyping
// todo try adding a limited form of subtyping in Fides

// todo delete ID, use subtyping between Identifer and locations, and delete implicit conversions.
private[fides2024] sealed trait ID protected(val uniqueID: Long) derives CanEqual:
  override def equals(obj: Any): Boolean = obj.asInstanceOf[Matchable] match
    case that: ID => this.uniqueID == that.uniqueID
    case _ => false
  override def hashCode(): Int = uniqueID.hashCode()
object ID:
  private val currentID = AtomicLong(0)
  private[values] def nextLong: Long = currentID.getAndIncrement()
end ID

/**
  * Identifiers are structureless. They can only be compared for equality. They cannot be inspected in any other way.
  * New identifiers can be created. It is not possible to construct identifiers in any other way.
  *
  * Due to hacky implicit conversions using asInstanceOf,
  * we effectively have Channel[T] < Identifier and Cell[T] < Identifier.
  */
final class Identifier private[values](uniqueID: Long) extends ID(uniqueID), Val[Identifier]:
  override def toString: String = s"Identifier(${hashCode()})"
object Identifier:
  def apply(): Identifier = new Identifier(ID.nextLong)
end Identifier
// todo should the Fides programmer be allowed to extend this class? Make it open?

/**
  * A type of location used for channels
  *
  * Due to hacky implicit conversions using asInstanceOf, we effectively have Channel[T] < Identifier.
  *
  * @tparam T the type of the values that transit through the channel
  */
final class Channel[T <: ValType] private(uniqueID: Long) extends ID(uniqueID), Val[Channel[T]]:
  override def toString: String = s"Channel(${hashCode()})"
object Channel:
  def apply[T <: ValType](): Channel[T] = new Channel(ID.nextLong)

  given Conversion[Channel[?], Identifier] with
    def apply(channel: Channel[?]): Identifier = new Identifier(channel.uniqueID)
end Channel

/**
  * A type of location used for memory cells
  *
  * Due to hacky implicit conversions using asInstanceOf, we effectively have Cell[T] < Identifier.
  *
  * @tparam T the type of the values that get stored in the cell
  */
final class Cell[T <: ValType] private(uniqueID: Long) extends ID(uniqueID), Val[Cell[T]]:
  override def toString: String = s"Cell(${hashCode()})"
object Cell:
  def apply[T <: ValType](): Cell[T] = new Cell(ID.nextLong)

  given Conversion[Cell[?], Identifier] with
    def apply(cell: Cell[?]): Identifier = new Identifier(cell.uniqueID)
end Cell

private[fides2024] sealed trait Key:
  def identifier: Identifier
  override def toString: String = s"Key(${identifier.hashCode()})"

/**
  * A key has a corresponding identifier. The identifer can be obtained from the key, but not vice versa
  * (there might not even exist a corresponding key for some identifiers).
  */
final class IdentifierKey extends Val[IdentifierKey], Key:
  val identifier: Identifier = Identifier()
end IdentifierKey

final class ChannelKey[T <: ValType] extends Val[IdentifierKey], Key:
  val channel: Channel[T] = Channel()
  def identifier: Identifier =
    import scala.language.implicitConversions
    channel
end ChannelKey

// todo add Symbol?
// todo add way to get reflected T, maybe using izumi-reflect
// todo use context functions?

// todo Identifiers are globally unique; their names (Symbol) are not, and bound to scopes.
