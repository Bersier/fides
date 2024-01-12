package fides2024.syntax.meta

import fides2024.syntax.code.{Code, CodeType, Expr, Ptrn, VarArgs, Xctr}
import fides2024.syntax.values.Collected

/**
  * Converts a Collected of code quotations to a Quoted of a VarArgs of all the pieces of code.
  */
final case class Zip[C <: CodeType](pieces: Code[Expr[Collected[Quoted[C]]]]) extends Expr[Quoted[VarArgs[C]]]

/**
  * Extracts the components out of a Concurrent component in the context of a refutable pattern.
  */
final case class UnZipPtrn[C <: CodeType]
(pieces: Code[Ptrn[Collected[Quoted[C]], Collected[Quoted[C]]]]) extends Ptrn[Quoted[VarArgs[C]], Quoted[VarArgs[C]]]

/**
  * Extracts the components out of a Concurrent component in the context of an irrefutable pattern.
  */
final case class UnZip[C <: CodeType](pieces: Code[Xctr[Collected[Quoted[C]]]]) extends Xctr[Quoted[VarArgs[C]]]
