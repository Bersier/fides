package fides.syntax.meta

import fides.syntax.core.Code
import fides.syntax.types.{CollectedUT, Expr, Exvr, QuotedT, TopS}

/**
  * Outputs an equivariantly updated version of the inputted term.
  */
final case class Update[S <: TopS](
  code: Code[Expr[QuotedT[S]]],
  f: [C <: TopS] => QuotedT[C] => QuotedT[C], // todo f should be a Fides function, not a Scala function
) extends Code[Exvr[QuotedT[S]]]

/**
  * Outputs the children of the inputted term.
  */
final case class Children(code: Code[Expr[QuotedT[?]]]) extends Code[Exvr[CollectedUT[QuotedT[?]]]]
