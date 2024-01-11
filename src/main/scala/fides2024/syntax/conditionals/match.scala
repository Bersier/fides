package fides2024.syntax.conditionals

import fides2024.syntax.kinds.{Code, Ptrn, ValType, Xctr}

/**
  * Tries to match a value to the given pattern. Upon failure, outputs the value to the alternative instead.
  */
final case class Match[T <: ValType](pattern: Code[Ptrn[T, T]], alternative: Code[Xctr[T]]) extends Xctr[T]
