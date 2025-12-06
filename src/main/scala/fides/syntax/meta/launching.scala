package fides.syntax.meta

import fides.syntax.machinery.*

/**
  * Launches [[code]] as a new process, and outputs a signed value of the code, confirming the launch.
  *
  * In the code, all the [[Channel]]s used in [[InpChan]]s undergo automatic renaming.
  * The new names are visible in the outputted code certificate.
  */
final case class Launch(code: OldCode[ExprG[QuoteD[AplrG]]]) extends OldCode[ExvrG[SignedD[QuoteD[AplrG]]]]

// todo Catchable and Catch
