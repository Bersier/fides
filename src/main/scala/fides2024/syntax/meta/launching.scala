package fides2024.syntax.meta

import fides2024.syntax.kinds.{Code, Component, Expr}
import fides2024.syntax.signatures.Signed

/**
  * Launches the inputted code as a new process, and outputs a signed value of the code, confirming the launch.
  */
final case class Launch(code: Code[Expr[Quoted[Component]]]) extends Expr[Signed[Quoted[Component]]]

/**
  * Launches a new sandboxed process, and outputs a signed value of the monitor's code, confirming the launch.
  */
final case class LaunchSandboxed(
  monitor: Code[Expr[Quoted[Component]]],
  sandboxed: Code[Expr[Quoted[Component]]],
) extends Expr[Signed[Quoted[Component]]]
