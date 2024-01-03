package fides2024.syntax.values

import fides2024.syntax.*

/**
  * Identifiers are structureless. They can only be compared for equality. They cannot be inspected in any other way.
  * New identifiers can be created. It is not possible to construct identifiers in any other way.
  */
final class ID[T <: IDType] extends Val[ID[T]] derives CanEqual

/**
  * A type of location used for channels
  *
  * @tparam T the type of the values that transit through the channel
  */
type Channel[T <: ValType] = ID[ChannelType[T]]
object Channel:
  def apply[T <: ValType](): Channel[T] = ID[ChannelType[T]]()
end Channel

// todo use implicits to somehow keep track of the types? Or use member types?

/**
  * A type of location used for memory cells
  *
  * @tparam T the type of the values that get stored in the cell
  */
type Cell[T <: ValType] = ID[CellType[T]]
object Cell:
  def apply[T <: ValType](): Cell[T] = ID[CellType[T]]()
end Cell

/**
  * A key has a corresponding identifier. The identifer can be obtained from the key, but not vice versa
  * (there might not even exist a corresponding key for some identifiers).
  */
final class IDKey[T <: IDType] extends Val[IDKey[T]]:
  val identifier: ID[T] = new ID[T]
end IDKey

// todo add Symbol?
// todo add way to get reflected T, maybe using izumi-reflect
// todo use context functions?

// todo Identifiers are globally unique; their names (Symbol) are not, and bound to scopes.
