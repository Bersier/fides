package v4

sealed trait Val // Move Val and subtypes to its own file
sealed class Address extends Val
final class Name extends Val

// The advantage of making these addresses, instead of primitives, is that we don't have to worry about dangling
// primitives (which would be needed to simulate these).
// However, if they were local primitives, they would allow more explicit control...
// They would make clear what can happen to this named scope...
// In that case, however, the control primitives could not themselves be controlled...
sealed trait Command extends Address
final class Start(name: Name) extends Command // Play / Run / Execute / Resume
final class Pause(name: Name) extends Command // Halt / Freeze
final class Kill(name: Name) extends Command  // Kill / Stop / Terminate

// This one is a bit different from the others. To execute the others, one just needs to send 'unit' to them.
// For this one, the destination must be specified.
final class Move(scope: Name) extends Command


sealed trait BoolVal extends Val
object True extends Command
object False extends Command

final case class APair()

final case class Signed private(contents: Val, signatory: Signatory) extends Val {
  def this(message: Address, signatoryKey: SignatoryKey) = this(message, signatoryKey.signatory)
}

sealed trait Signatory extends Val

final class SignatoryKey() extends Signatory {
  def signatory: Signatory = this
}

final case class ScalaVal[T] private(private val value: T) extends Val
object ScalaVal {
  implicit def pack[T](value: T): ScalaVal[T] = ScalaVal(value)
  implicit def unpack[T](scalaVal: ScalaVal[T]): T = scalaVal match {
    case ScalaVal(value) => value
  }
}
