package v4

sealed trait Val
sealed class Address extends Val
final class Name extends Val

sealed trait Command extends Address
final class Start(name: Name) extends Command
final class Pause(name: Name) extends Command
final class Dissolve(name: Name) extends Command
final class Kill(name: Name) extends Command

final class Move(scope: Name) extends Command


sealed trait BoolVal extends Val
object True extends BoolVal
object False extends BoolVal

final case class APair()

final case class Signed private(contents: Val, signatory: Signatory) extends Val {
  def this(message: Address, signatoryKey: SignatoryKey) = this(message, signatoryKey.signatory)
}

sealed trait Signatory extends Val

final class SignatoryKey() extends Signatory {
  def signatory: Signatory = this
}
