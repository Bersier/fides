package fides.syntax.meta

import fides.syntax.machinery.*

/**
  * Wraps a value into a Quoted.
  */
final case class Wrap[
  D <: TopD,
  G <: ExprHG[D],
  M <: GenHM[G],
](value: Code[M]) extends Code[WrapM[D, G, M]]

/**
  * Evaluates a quoted expression.
  */
final case class Eval[
  D <: TopD,
  G <: ExprG[QuoteD[GenM[ExprHG[D]]]],
  M <: GenHM[G],
](value: Code[M]) extends Code[EvalM[D, G, M]]

/* todo
 *
 * With the expanded power of metaprogramming, I think that now,
 * the Q associated with a quote is not as directly related to the Q of the polar of the quote.
 *
 * Actually, the system we have simply cannot support this power :S We need to move to located quotes.
 */
