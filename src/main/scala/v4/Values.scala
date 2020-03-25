package v4

sealed trait Val // Move Val and subtypes to its own file
final class Address extends Val

sealed trait Command extends Val
object Start extends Command
object Pause extends Command
object Kill extends Command

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
