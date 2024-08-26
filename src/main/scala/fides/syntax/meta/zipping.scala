package fides.syntax.meta

import fides.syntax.code.{Code, CodeType, Expr, Ptrn, Xctr}
import fides.syntax.values.CollectedG

/**
  * Used for unordered collections of pieces of code, at the syntax level.
  */
type Args[+S <: CodeType] = ArgsG[Boolean, S]
object Args:
  def apply[S <: CodeType](): ArgsG[false, Nothing] = None
  def apply[S <: CodeType](first: Code[S], others: Code[S]*): ArgsG[true, S] = new Some(first, others*)
  case object None extends ArgsG[false, Nothing]:
    def arguments: Iterable[Code[Nothing]] = Iterable.empty
  type None = None.type
  final class Some[+S <: CodeType](first: Code[S], others: Code[S]*) extends ArgsG[true, S]:
    val arguments: Iterable[Code[S]] = first +: others
  end Some
end Args

sealed trait ArgsG[+IsNonEmpty <: Boolean, +S <: CodeType] extends Code[ArgsG[IsNonEmpty, S]], CodeType:
  def arguments: Iterable[Code[S]]
  override def toString: String = s"Args($arguments)"
end ArgsG

/**
  * Converts a Collected of code quotations to a Quoted of a VarArgs of all the pieces of code.
  */
final case class Zip[IsNonEmpty <: Boolean, S <: CodeType](
  pieces: Code[Expr[CollectedG[IsNonEmpty, Quoted[S]]]],
) extends Expr[Quoted[ArgsG[IsNonEmpty, S]]]

/**
  * Extracts the processes out of a Concurrent process in the context of an irrefutable pattern.
  */
final case class UnZip[IsNonEmpty <: Boolean, S <: CodeType](
  pieces: Code[Xctr[CollectedG[IsNonEmpty, Quoted[S]]]],
) extends Xctr[Quoted[ArgsG[IsNonEmpty, S]]]

/**
  * Extracts the processes out of a Concurrent process in the context of a refutable pattern.
  */
final case class MatchZip[IsNonEmpty <: Boolean, S <: CodeType](
  pieces: Code[Ptrn[CollectedG[IsNonEmpty, Quoted[S]], CollectedG[IsNonEmpty, Quoted[S]]]],
) extends Ptrn[Quoted[ArgsG[IsNonEmpty, S]], Quoted[ArgsG[IsNonEmpty, S]]]
// todo should this really be allowed?
