package fides.syntax.meta

import fides.syntax.code.{Code, CodeType, Expr, Ptrn, Xctr}
import fides.syntax.values.Collected

/**
  * Used for unordered collections of pieces of code, at the syntax level.
  */
final case class VarArgs[+S <: CodeType](pieces: Code[S]*) extends Code[VarArgs[S]], CodeType

/**
  * Converts a Collected of code quotations to a Quoted of a VarArgs of all the pieces of code.
  */
final case class Zip[S <: CodeType](pieces: Code[Expr[Collected[Quoted[S]]]]) extends Expr[Quoted[VarArgs[S]]]

/**
  * Extracts the processes out of a Concurrent process in the context of an irrefutable pattern.
  */
final case class UnZip[S <: CodeType](pieces: Code[Xctr[Collected[Quoted[S]]]]) extends Xctr[Quoted[VarArgs[S]]]

/**
  * Extracts the processes out of a Concurrent process in the context of a refutable pattern.
  */
final case class MatchZip[S <: CodeType]
(pieces: Code[Ptrn[Collected[Quoted[S]], Collected[Quoted[S]]]]) extends Ptrn[Quoted[VarArgs[S]], Quoted[VarArgs[S]]]
