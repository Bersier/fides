package syntax3

object Syntax {
  type Multiset[A] = Map[A, BigInt]

  sealed trait Process

  trait Primitive extends Process
  final case class Constant(value: Val, out: Address) extends Primitive
  final case class NewIdee(ideeOut: Address) extends Primitive

  final case class Send(packetIn: Port, toIn: Port) extends Primitive
  final case class SendToAll(packetIn: Port, toIn: Port) extends Primitive
  final case class Receive(fromIn: Port, onSetupOut: Address, packetOut: Address) extends Primitive

  final case class GetPort(ideeIn: Port, portOut: Address) extends Primitive
  final case class GetAddress(ideeIn: Port, addressOut: Address) extends Primitive

  final case class Sign(messageIn: Port, signatoryIn: Port, signedMessageOut: Address) extends Primitive
  final case class UnpackSigned(signedIn: Port, messageOut: Address, signatoryOut: Address) extends Primitive

  final case class Wait(in: Port, startIn: Port, out: Address) extends Primitive
  final case class Fork(valIn: Port, valOut1: Address, valOut2: Address) extends Primitive

  final case class IfEqual(valIn1: Port, valIn2: Port, ifBranch: Process, elseBranch: Process) extends Primitive
  //final case class GetType(valIn: Port, )

  final case class Par(processes: Multiset[Process]) extends Process
  final case class Replicated(process: Process) extends Process
  final case class MayInterrupt(run: Port, stop: Port, kill: Port, process: Process) extends Process

  final class BehaviorName

  sealed trait Val

  object Unit extends Val

  final case class Tuple(values: IndexedSeq[XPut]) extends Val

  final case class Signed private(contents: Address, signatory: Address) extends Val {
    def this(message: Address, signatory: Idee) = this(message, signatory.address)
  }

  final case class Integer(value: BigInt) extends Val

  sealed trait XPut extends Val

  sealed trait Port extends XPut
  sealed trait Address extends XPut

  final class Idee private extends Port with Address {
    def port: Port = this
    def address: Address = this
  }

  object Idee {
    def makeNew: Idee = {
      val result = new Idee
      result
    }
  }
}
