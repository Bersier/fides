package fides.syntax.meta

import fides.syntax.types.*

/**
  * Launches [[code]] as a new process, and outputs a signed value of the code, confirming the launch.
  *
  * In the code, all the [[Channel]]s used in [[InpChan]]s undergo automatic renaming.
  * The new names are visible in the outputted code certificate.
  */
final case class Launch(code: OldCode[Expr[QuoteD[Aplr]]]) extends OldCode[Exvr[SignedD[QuoteD[Aplr]]]]

// todo Catchable and Catch
