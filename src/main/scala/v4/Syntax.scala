package v4

object Syntax {
  type Multiset[A] = Map[A, BigInt]

  sealed trait Loc
  type OutLoc = Loc
  type InLoc = Loc

  sealed trait Val
  final class Address extends Val

  sealed trait Process
  sealed trait Primitive extends Process

  // final case class Code(process: Process)

  final case class Constant(value: Val, outLoc: OutLoc) extends Primitive

  final case class Send(message: InLoc, to: InLoc) extends Primitive
//  /** @param address A fixed address (not an InLoc) */ // In that case, don't really need it.
//  final case class Receive(message: OutLoc, address: Address) extends Primitive

  final case class Forward(inLoc: InLoc, outLoc: OutLoc) extends Primitive

  /** Can only be used to broadcast to locs in this code, not external ones */
  final case class Broadcast(inLoc: InLoc, outLoc: OutLoc) extends Primitive

  final case class Wait(token: InLoc, inLoc: InLoc, outLoc: OutLoc) extends Primitive

  // Should Sign use a fixed signatory, similarly to (late) Receive (signatory: Signatory)?
  final case class Sign(message: InLoc, signatory: InLoc, signedMessage: OutLoc) extends Primitive
  final case class Unsign(signedMessage: InLoc, message: OutLoc, signatory: OutLoc) extends Primitive

  final case class Join(first: InLoc, second: InLoc, pair: OutLoc) extends Primitive
  final case class Fork(pair: InLoc, first: OutLoc, second: OutLoc) extends Primitive

  final case class Equals(first: InLoc, second: InLoc, equal: OutLoc) extends Primitive

  final case class Parallel(processes: Multiset[Process]) extends Process
  final case class Replicated(addresses: Set[Address], process: Process) extends Process

  final case class Awake(command: InLoc, process: Process) extends Process
  final case class Asleep(command: InLoc, process: Process) extends Process

  final case class Branch(boolean: InLoc, trueBranch: Process, falseBranch: Process) extends Process

  // Swappable(process, address)
  // Swap(process: InLoc, at: InLoc |?| Address)

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

  // Swap

  /**
    * For convenience, so that we don't need to reimplement basics.
    */
  final case class Execute[A, B](input: InLoc, function: A => B, output: OutLoc) extends Process

  final case class ScalaVal[T] private(private val value: T) extends Val
  object ScalaVal {
    implicit def pack[T](value: T): ScalaVal[T] = ScalaVal(value)
    implicit def unpack[T](scalaVal: ScalaVal[T]): T = scalaVal match {
      case ScalaVal(value) => value
    }
  }
}
