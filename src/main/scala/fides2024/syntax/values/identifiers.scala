package fides2024.syntax.values

import fides2024.syntax.*

// todo if only we could secretly make Identifier, Channel and Cell the same type. But I think value classes are too
//  restrictive. Still, we should try using value classes.

// todo try using a hacky secret ID supertype
private transparent sealed class ID

// todo use opaque type, and use type class instead of 'extends Val'?

/**
  * Identifiers are structureless. They can only be compared for equality. They cannot be inspected in any other way.
  * New identifiers can be created. It is not possible to construct identifiers in any other way.
  */
final case class Identifier private[values](private val uniqueID: UniqueID) extends Val[Identifier] derives CanEqual
object Identifier:
  def apply(): Identifier = Identifier(UniqueID())
end Identifier

// todo in-Fides conversion from channels to identifiers
final case class ChannelIdentifier(channel: Code[Expr[Channel[?]]]) extends Expr[Identifier]
// todo similarly for Cells

/**
  * A type of location used for channels
  *
  * @tparam T the type of the values that transit through the channel
  */
final case class Channel[T <: ValType] private(private val uniqueID: UniqueID) extends Val[Channel[T]]
object Channel:
  def apply[T <: ValType](): Channel[T] = Channel(UniqueID())
  given Conversion[Channel[?], Identifier] with
    def apply(channel: Channel[?]): Identifier =
      Identifier(channel.uniqueID)
end Channel

/**
  * A type of location used for memory cells
  *
  * @tparam T the type of the values that get stored in the cell
  */
final case class Cell[T <: ValType] private(private val uniqueID: UniqueID) extends Val[Cell[T]]
object Cell:
  def apply[T <: ValType](): Cell[T] = Cell(UniqueID())
  given Conversion[Cell[?], Identifier] with
    def apply(cell: Cell[?]): Identifier =
      Identifier(cell.uniqueID)
end Cell

sealed trait Key:
  def identifier: Identifier

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

private final class UniqueID

// todo add Symbol?
// todo add way to get reflected T, maybe using izumi-reflect
// todo use context functions?

// todo Identifiers are globally unique; their names (Symbol) are not, and bound to scopes.
