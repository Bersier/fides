package fides.syntax.meta

import fides.syntax.code.{Code, Expr, Process}
import fides.syntax.identifiers.Identifier
import fides.syntax.signatures.Signed

/**
  * Launches [[code]] as a new process, and outputs a signed value of the code, confirming the launch.
  *
  * In the code, all the the [[Channel]]s used in [[InpChan]]s undergo automatic renaming.
  * The new names are visible in the outputted code certificate.
  */
final case class Launch(code: Code[Expr[Quoted[Process]]]) extends Expr[Signed[Quoted[Process]]]

case object Launcher extends Identifier("Launcher")
