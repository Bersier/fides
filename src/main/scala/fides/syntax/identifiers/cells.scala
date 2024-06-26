package fides.syntax.identifiers

import fides.syntax.code.{Code, Expr, Process, Val, ValType, Xctr}
import fides.syntax.values.Pulse
import izumi.reflect.Tag

/**
  * A type of location used for memory cells
  *
  * A cell guarantees that there is a way to temporally order operations made on it
  * in a way that is consistent with observations.
  *
  * @tparam T the type of the values that get stored in the cell
  */
final class Cell[T <: ValType : Tag] private(
  var value: Code[Val[T]],
  name: String,
) extends Identifier(name), Val[Cell[T]], Process:
  override def toString: String = s"$$$name($value)"
  def valueType: Tag[T] = summon[Tag[T]]
object Cell:
  def apply[T <: ValType](value: Code[Val[T]])(using Context, Tag[T]): Cell[T] =
    Location.from(new Cell(value, _))
  def apply[T <: ValType](value: Code[Val[T]], name: String)(using Context, Tag[T]): Cell[T] =
    Location.from(new Cell(value, _), name)
end Cell

/**
  * Reads the value contained in the cell once the trigger value is available.
  */
final case class Read[T <: ValType](trigger: Code[Expr[Pulse]], iD: Code[Val[Cell[T]]]) extends Expr[T]

/**
  * Unconditionally overwrites the value contained in the cell, and signals completion.
  */
final case class Write[T <: ValType](signal: Code[Xctr[Pulse]], iD: Code[Val[Cell[T]]]) extends Xctr[T]

/**
  * Atomically
  * <ol>
  * <li>Reads the value of the cell, V.
  * <li>Compares V to [[testValue]].
  * <li>If they are the same, writes [[newValue]] to the cell.
  * <li>Outputs V.
  * </ol>
  * <p>
  * Atomically compares the current value of the cell with the inputted pattern and,
  * only if they are the same, updates the value of the cell to the inputted new value, and
  * outputs the previous value of the cell.
  */
final case class CompareAndSwap[T <: ValType](
  testValue: Code[Expr[T]],
  newValue: Code[Expr[T]],
  iD: Code[Val[Cell[T]]],
) extends Expr[T]
