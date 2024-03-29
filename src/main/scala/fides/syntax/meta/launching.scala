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
// todo having to look at the code certificate to find the new names is not convenient...
//  should the Launcher additionally output a list of renamings?

case object Launcher extends Identifier("Launcher")

/**
  * Launches a new sandboxed process, and outputs a signed value of the monitor's code, confirming the launch.
  *
  * Doesn't fundamentally add more trust power, even though, in the direct sense, it is more general than Launch.
  * This is because Launch is already a universal trusted third party, so, via Launch, a new trusted third party can
  * be created that does the same as LaunchSandboxed.
  */
final case class LaunchSandboxed(
  monitor: Code[Expr[Quoted[Process]]],
  sandboxed: Code[Expr[Quoted[Process]]],
) extends Expr[Signed[Quoted[Process]]]
// todo the identifiers linking the monitor to the the sandboxed need to be present in the signed content,
//  otherwise, this is meaningless.

// todo Catch
