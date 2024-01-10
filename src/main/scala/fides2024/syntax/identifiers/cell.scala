package fides2024.syntax.identifiers

import fides2024.syntax.{Val, ValType}

/**
  * A type of location used for memory cells
  *
  * A cell guarantees that there is a way to temporally order operations made on it
  * in a way that is consistent with observations.
  *
  * @tparam T the type of the values that get stored in the cell
  */
open class Cell[T <: ValType] protected(name: String) extends Identifier(name), Val[Cell[T]]:
  override def toString: String = s"\\$$name"
object Cell extends LocationBuilder[Cell]:
  protected def constructor[T <: ValType](name: String): Cell[T] = new Cell(name)
end Cell
// todo Does it need an initial value? I think so.
