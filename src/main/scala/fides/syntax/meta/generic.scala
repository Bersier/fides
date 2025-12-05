package fides.syntax.meta

import fides.syntax.types.*

/**
  * Outputs an equivariantly updated version of the inputted term.
  */
final case class Update[S <: TopS](
  code: OldCode[Expr[QuoteD[S]]],
  f: [C <: TopS] => QuoteD[C] => QuoteD[C], // todo f should be a Fides function, not a Scala function
  // todo for general transformations, a quotation of the exact same type cannot always be returned.
  //  In general, f should return a value compatible with the hole at that position...
  //  Examples: Increment constants, remove annotations, rename variables (i.e. change location IDs)
  //  https://gemini.google.com/app/af7edbf3eda7d3a3
) extends OldCode[Exvr[QuoteD[S]]]

/**
  * Outputs the children of the inputted term.
  */
final case class Children(code: OldCode[Expr[QuoteD[?]]]) extends OldCode[Exvr[CollectedUD[QuoteD[?]]]]
