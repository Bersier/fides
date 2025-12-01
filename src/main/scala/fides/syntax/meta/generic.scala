package fides.syntax.meta

import fides.syntax.types.*

/**
  * Outputs an equivariantly updated version of the inputted term.
  */
final case class Update[S <: TopS](
  code: Code[Expr[QuotedT[S]]],
  f: [C <: TopS] => QuotedT[C] => QuotedT[C], // todo f should be a Fides function, not a Scala function
  // todo for general transformations, a quotation of the exact same type cannot always be returned.
  //  In general, f should return a value compatible with the hole at that position...
  //  Examples: Increment constants, remove annotations, rename variables (i.e. change location IDs)
) extends Code[Exvr[QuotedT[S]]]

/**
  * Outputs the children of the inputted term.
  */
final case class Children(code: Code[Expr[QuotedT[?]]]) extends Code[Exvr[CollectedUT[QuotedT[?]]]]
