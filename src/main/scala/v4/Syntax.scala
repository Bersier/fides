package v4

object Syntax {
  type Multiset[A] = Map[A, BigInt]

  trait Val // Should be a type class, ideally
  type InLoc
  type OutLoc
  type AddressVar // Is owned by a Receive.

  sealed trait Process
  trait Primitive extends Process

  final case class Constant(value: Val, outLoc: OutLoc) extends Primitive
  
  final case class Send(/*previous: InLoc, */message: InLoc, to: InLoc) extends Primitive
  final case class Receive(message: OutLoc, address: AddressVar) extends Primitive
//  final case class Receive(message: OutLoc, process: message.Process) extends Primitive     ?

  final case class Broadcast(inLoc: InLoc, outLoc: OutLoc) extends Primitive

  final case class Wait(token: InLoc, inLoc: InLoc, outLoc: OutLoc) extends Primitive

  final case class Sign(message: InLoc, signatory: InLoc, signedMessage: OutLoc) extends Primitive
  final case class Unsign(signedMessage: InLoc, message: OutLoc, signatory: OutLoc) extends Primitive

  final case class Pair(first: InLoc, second: InLoc, pair: OutLoc) extends Primitive
  final case class Unpair(pair: InLoc, first: OutLoc, second: OutLoc) extends Primitive

//  final case class Equals(first: InLoc, second: InLoc, equal: OutLoc) extends Primitive

  final case class BranchOnEqual(first: InLoc, second: InLoc, equalBranch: Process, unequalBranch: Process)
    extends Primitive

  final case class Parallel(processes: Multiset[Process]) extends Process
  final case class Replicated(process: Process) extends Process // TODO Semantics coverage of interaction between Replicated and Receive
  final case class Interruptible(start: InLoc, stop: InLoc, kill: InLoc, process: Process) extends Process
  final case class Swappable()

  /**
    * For convenience, so that we don't need to reimplement basics.
    */
  final case class Execute(input: InLoc, lambda: Val => Val, output: OutLoc) extends Process

  final case class ScalaVal[T](value: T)
  object ScalaVal {
    implicit def pack[T](value: T): ScalaVal[T] = ScalaVal(value)
    implicit def unpack[T](scalaVal: ScalaVal[T]): T = scalaVal match {
      case ScalaVal(value) => value
    }
  }
}
