package fides.syntax.meta

import fides.syntax.code.{Code, CodeType, Expr, Ptrn, Xctr}
import fides.syntax.values.Collected

/**
  * Used for unordered collections of pieces of code, at the syntax level.
  */
final case class VarArgs[+C <: CodeType](pieces: Code[C]*) extends Code[VarArgs[C]], CodeType

/**
  * Converts a Collected of code quotations to a Quoted of a VarArgs of all the pieces of code.
  */
final case class Zip[C <: CodeType](pieces: Code[Expr[Collected[Quoted[C]]]]) extends Expr[Quoted[VarArgs[C]]]

/**
  * Extracts the processes out of a Concurrent process in the context of a refutable pattern.
  */
final case class UnZipPtrn[C <: CodeType]
(pieces: Code[Ptrn[Collected[Quoted[C]], Collected[Quoted[C]]]]) extends Ptrn[Quoted[VarArgs[C]], Quoted[VarArgs[C]]]

/**
  * Extracts the processes out of a Concurrent process in the context of an irrefutable pattern.
  */
final case class UnZip[C <: CodeType](pieces: Code[Xctr[Collected[Quoted[C]]]]) extends Xctr[Quoted[VarArgs[C]]]
