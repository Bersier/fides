package fides.syntax.meta

import fides.syntax.core.Code
import fides.syntax.identifiers.Identifier
import fides.syntax.types.{Expr, Process, QuotedT, SignedT}

/**
  * Launches [[code]] as a new process, and outputs a signed value of the code, confirming the launch.
  *
  * In the code, all the [[Channel]]s used in [[InpChan]]s undergo automatic renaming.
  * The new names are visible in the outputted code certificate.
  */
final case class Launch(code: Code[Expr[QuotedT[Process]]]) extends Code[Expr[SignedT[QuotedT[Process]]]]

case object Launcher extends Identifier
