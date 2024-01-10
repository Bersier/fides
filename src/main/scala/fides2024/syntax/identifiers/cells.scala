package fides2024.syntax.identifiers

import fides2024.syntax.kinds.{Code, Expr, Val, ValType, Xctr}
import fides2024.syntax.values.U

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

/**
  * Reads the value contained in the cell once the trigger value is available.
  */
final case class Read[T <: ValType](trigger: Code[Expr[U.type]], iD: Code[Val[Cell[T]]]) extends Expr[T]:
  // todo only when trigger is trivial: override def toString: String = s"[${internalIDString(iD)}]"
end Read

/**
  * Unconditionally overwrites the value contained in the cell, and signals completion.
  */
final case class Write[T <: ValType](signal: Code[Xctr[U.type]], iD: Code[Val[Cell[T]]]) extends Xctr[T]:
  // todo only when trigger is trivial: override def toString: String = s"[|${internalIDString(iD)}|]"
end Write

/**
  * Atomically
  *  1. Reads the value of the cell, V.
  *  2. Compares V to @testValue.
  *  3. If they are the same, writes @newValue to the cell.
  *  4. Outputs V.
  *
  * Atomically compares the current value of the cell with the inputted pattern and,
  * only if they are the same, updates the value of the cell to the inputted new value, and
  * outputs the previous value of the cell.
  */
final case class CompareAndSwap[T <: ValType]
(testValue: Code[Expr[T]], newValue: Code[Expr[T]], iD: Code[Val[Cell[T]]]) extends Expr[T]
