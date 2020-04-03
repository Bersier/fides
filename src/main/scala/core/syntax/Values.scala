package core.syntax

trait Val
final class Name extends Val
sealed class Address[T <: Val] extends Loc[T] with Val

final class PrivateAddress[T <: Val] extends Address[T] {
  def address: Address[T] = this
}
final case class Broadcast[T <: Val](address: PrivateAddress[T]) extends Address[T]

sealed trait Command[T <: Val] extends Address[T]
final class Start(name: Name) extends Command[Unit.type]
final class Pause(name: Name) extends Command[Unit.type]
final class Dissolve(name: Name) extends Command[Unit.type]
final class Kill(name: Name) extends Command[Unit.type]
final class Move(name: Name) extends Command[Destination]

final class Destination(name: Name) extends Val

final case class APair[S <: Val, T <: Val](first: S, second: T) extends Val

final case class Code(process: Process) extends Val
final case class Patt(pattern: Pattern) extends Val

final case class Signed[T <: Val] private(contents: T, signatory: Signatory) extends Val {
  def this(message: T, signatoryKey: SignatoryKey) = this(message, signatoryKey.signatory)
}

sealed trait Signatory extends Val
final class SignatoryKey() extends Signatory {
  def signatory: Signatory = this
}

sealed trait Error extends Val