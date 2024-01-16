package fides2024.syntax.meta

import fides2024.syntax.code.{Code, Process, Expr}
import fides2024.syntax.signatures.Signed

/**
  * Launches the inputted code as a new process, and outputs a signed value of the code, confirming the launch.
  */
final case class Launch(code: Code[Expr[Quoted[Process]]]) extends Expr[Signed[Quoted[Process]]]

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
