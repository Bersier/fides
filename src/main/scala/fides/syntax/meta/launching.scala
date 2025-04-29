package fides.syntax.meta

import fides.syntax.core.Code
import fides.syntax.types.{Expr, Process, SignedT}
import fides.syntax.identifiers.Identifier

/**
  * Launches [[code]] as a new process, and outputs a signed value of the code, confirming the launch.
  *
  * In the code, all the [[Channel]]s used in [[InpChan]]s undergo automatic renaming.
  * The new names are visible in the outputted code certificate.
  */
final case class Launch(code: Code[Expr[Quoted[Process]]]) extends Expr[SignedT[Quoted[Process]]]

case object Launcher extends Identifier
