package fides.syntax.meta

import fides.syntax.code.{Code, CodeType, Expr, Ptrn, Xctr}
import fides.syntax.values.Collected

/**
  * Used for unordered collections of pieces of code, at the syntax level.
  */
sealed trait Args[+S <: CodeType] extends Code[Args[S]], CodeType:
  def arguments: Iterable[Code[S]]
object Args:
  def apply[S <: CodeType](arguments: Code[S]*): Args[S] =
    if arguments.isEmpty then None else Some(arguments*)
  case object None extends Args[Nothing], Code[None.type], CodeType:
    def arguments: Iterable[Code[Nothing]] = Iterable.empty[Code[Nothing]]
  end None
  final case class Some[+S <: CodeType](arguments: Code[S]*) extends Args[S], Code[Some[S]], CodeType:
    assert(arguments.nonEmpty)
  end Some
end Args

/**
  * Converts a Collected of code quotations to a Quoted of a VarArgs of all the pieces of code.
  */
final case class Zip[S <: CodeType](pieces: Code[Expr[Collected[Quoted[S]]]]) extends Expr[Quoted[Args[S]]]

final case class ZipNonEmpty[S <: CodeType]
(pieces: Code[Expr[Collected.Some[Quoted[S]]]]) extends Expr[Quoted[Args.Some[S]]]
// todo how to make the "Some" part generic?

/**
  * Extracts the processes out of a Concurrent process in the context of an irrefutable pattern.
  */
final case class UnZip[S <: CodeType](pieces: Code[Xctr[Collected[Quoted[S]]]]) extends Xctr[Quoted[Args[S]]]

final case class UnZipNonEmpty[S <: CodeType]
(pieces: Code[Xctr[Collected.Some[Quoted[S]]]]) extends Xctr[Quoted[Args.Some[S]]]

/**
  * Extracts the processes out of a Concurrent process in the context of a refutable pattern.
  */
final case class MatchZip[S <: CodeType]
(pieces: Code[Ptrn[Collected[Quoted[S]], Collected[Quoted[S]]]]) extends Ptrn[Quoted[Args[S]], Quoted[Args[S]]]
