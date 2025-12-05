package fides.syntax.meta

import fides.syntax.types.*

/**
  * Outputs an equivariantly updated version of the inputted term.
  */
final case class Update[S <: TopS](
  code: OldCode[ExprS[QuoteD[S]]],
  f: [M <: TopS] => QuoteD[M] => QuoteD[M], // todo f should be a Fides function, not a Scala function
  // todo for general transformations, a quotation of the exact same type cannot always be returned.
  //  In general, f should return a value compatible with the hole at that position...
  //  Examples: Increment constants, remove annotations, rename variables (i.e. change location IDs)
  //  https://gemini.google.com/app/af7edbf3eda7d3a3
) extends OldCode[ExvrS[QuoteD[S]]]

/**
  * Outputs the children of the inputted term.
  */
final case class Children(code: OldCode[ExprS[QuoteD[?]]]) extends OldCode[ExvrS[CollectedUD[QuoteD[?]]]]
