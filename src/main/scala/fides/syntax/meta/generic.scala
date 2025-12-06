package fides.syntax.meta

import fides.syntax.machinery.*

/**
  * Outputs an equivariantly updated version of the inputted term.
  */
final case class Update[G <: TopG](
  code: OldCode[OldExprG[QuoteD[G]]],
  f: [M <: TopG] => QuoteD[M] => QuoteD[M], // todo f should be a Fides function, not a Scala function
  // todo for general transformations, a quotation of the exact same type cannot always be returned.
  //  In general, f should return a value compatible with the hole at that position...
  //  Examples: Increment constants, remove annotations, rename variables (i.e. change location IDs)
  //  https://gemini.google.com/app/af7edbf3eda7d3a3
) extends OldCode[ExvrG[QuoteD[G]]]

/**
  * Outputs the children of the inputted term.
  */
final case class Children(code: OldCode[OldExprG[QuoteD[?]]]) extends OldCode[ExvrG[CollectedUD[QuoteD[?]]]]
