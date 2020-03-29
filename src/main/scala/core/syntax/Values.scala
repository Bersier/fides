package core.syntax

final class Name

trait Val
sealed class Address extends Val

sealed trait PrivateAddress extends Address
final case class Broadcast(address: PrivateAddress) extends Address

sealed trait Command extends Address
final class Start(name: Name) extends Command
final class Pause(name: Name) extends Command
final class Dissolve(name: Name) extends Command
final class Kill(name: Name) extends Command
final class Move(name: Name) extends Command

final case class APair(first: Val, second: Val) extends Val
final case class Code(process: Process) extends Val

final case class Signed private(contents: Val, signatory: Signatory) extends Val {
  def this(message: Address, signatoryKey: SignatoryKey) = this(message, signatoryKey.signatory)
}

sealed trait Signatory extends Val
final class SignatoryKey() extends Signatory {
  def signatory: Signatory = this
}
