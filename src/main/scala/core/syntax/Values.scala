package core.syntax

final class Name

trait Val
sealed class Address extends Loc[Val] with Val

final class PrivateAddress extends Address
final case class Broadcast(address: PrivateAddress) extends Address

sealed trait Command extends Address
final class Start(name: Name) extends Loc[Unit.type] with Command
final class Pause(name: Name) extends Loc[Unit.type] with Command
final class Dissolve(name: Name) extends Loc[Unit.type] with Command
final class Kill(name: Name) extends Loc[Unit.type] with Command
final class Move(name: Name) extends Loc[Destination] with Command

final class Destination(name: Name) extends Val

final case class APair[S <: Val, T <: Val](first: S, second: T) extends Val
final case class Code(process: Process) extends Val

final case class Signed private(contents: Val, signatory: Signatory) extends Val {
  def this(message: Address, signatoryKey: SignatoryKey) = this(message, signatoryKey.signatory)
}

sealed trait Signatory extends Val
final class SignatoryKey() extends Signatory {
  def signatory: Signatory = this
}
