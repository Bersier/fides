package fides2024.syntax.meta

import fides2024.syntax.components.Concurrent
import fides2024.syntax.kinds.{Code, Component, Expr, Ptrn, Xctr}
import fides2024.syntax.values.Collected

/**
  * Converts a Collected of component quotations to a Quoted of the components, composed concurrently.
  */
final case class Zip(components: Code[Expr[Collected[Quoted[Component]]]]) extends Expr[Quoted[Concurrent]]

/**
  * Extracts the components out of a Concurrent component in the context of a refutable pattern.
  */
final case class UnZipPtrn(
  components: Code[Ptrn[Collected[Quoted[Component]], Collected[Quoted[Component]]]],
) extends Ptrn[Quoted[Concurrent], Quoted[Concurrent]]

/**
  * Extracts the components out of a Concurrent component in the context of an irrefutable pattern.
  */
final case class UnZip(components: Code[Xctr[Collected[Quoted[Component]]]]) extends Xctr[Quoted[Concurrent]]
