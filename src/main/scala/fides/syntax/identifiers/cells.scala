package fides.syntax.identifiers

///**
//  * A type of location used for memory cells
//  *
//  * A cell guarantees that there is a way to temporally order operations made on it
//  * in a way that is consistent with observations.
//  *
//  * @tparam D the type of the values that get stored in the cell
//  */
//final class Cell[D <: ValTop : Tag] private(
//  var value: Code[Lit[D]],
//  name: String,
//) extends Identifier(name), Lit[Cell[D]], Process:
//  override def toString: String = s"$$$name($value)"
//  def valueType: Tag[D] = summon[Tag[D]]
//object Cell:
//  def apply[D <: ValTop](value: Code[Lit[D]])(using Context, Tag[D]): Cell[D] =
//    Named.from(new Cell(value, _))
//  def apply[D <: ValTop](value: Code[Lit[D]], name: String)(using Context, Tag[D]): Cell[D] =
//    Named.from(new Cell(value, _), name)
//end Cell
//
///**
//  * Reads the value contained in the cell once the trigger value is available.
//  */
//final case class Read[D <: ValTop](trigger: Code[Expr[Pulse]], iD: Code[Lit[Cell[D]]]) extends Expr[D]
//
///**
//  * Unconditionally overwrites the value contained in the cell, and signals completion.
//  */
//final case class Write[D <: ValTop](signal: Code[Xctr[Pulse]], iD: Code[Lit[Cell[D]]]) extends Xctr[D]
//
///**
//  * Atomically
//  * <ol>
//  * <li>Reads the value of the cell, V.
//  * <li>Compares V to [[testValue]].
//  * <li>If they are the same, writes [[newValue]] to the cell.
//  * <li>Outputs V.
//  * </ol>
//  * <p>
//  * Atomically compares the current value of the cell with the inputted pattern and,
//  * only if they are the same, updates the value of the cell to the inputted new value, and
//  * outputs the previous value of the cell.
//  */
//final case class CompareAndSwap[D <: ValTop](
//  testValue: Code[Expr[D]],
//  newValue: Code[Expr[D]],
//  iD: Code[Lit[Cell[D]]],
//) extends Expr[D]
