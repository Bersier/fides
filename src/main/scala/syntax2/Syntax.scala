package syntax2

object Syntax {
  type Multiset[A] = Map[A, BigInt]

  final case class Program(definitions: Set[Class], main: Process)

  final case class Class(name: ClassName, parameters: Set[Idee], body: Process)

  sealed trait Process

  trait Atom extends Process
  final case class Constant(value: Val, out: Idee) extends Atom
  final case class NewIdee(ideeOut: Idee) extends Atom
  final case class Send(packetIn: Idee, toIn: Idee) extends Atom
  final case class Receive(fromIn: Idee, onSetupOut: Idee, packetOut: Idee) extends Atom
  final case class GetPort(ideeIn: Idee, portOut: Idee) extends Atom
  final case class GetAddress(ideeIn: Idee, addressOut: Idee) extends Atom
  final case class UnpackSigned(signedIn: Idee, messageOut: Idee, signatoryOut: Idee) extends Atom
  final case class Sign(messageIn: Idee, signatureIn: Idee, signedMessageOut: Idee) extends Atom
  final case class Wait(in: Idee, startIn: Idee, out: Idee) extends Atom
  final case class Both(valIn: Idee, valOut1: Idee, valOut2: Idee) extends Atom

  final case class Apply(className: ClassName, arguments: Map[Idee, Idee]) extends Process

  final case class Par(processes: Multiset[Process]) extends Process
  final case class MayInterrupt(run: Idee, stop: Idee, kill: Idee, process: Process) extends Process

  final class ClassName

  sealed trait Val
  object Unit extends Val
  sealed trait Value extends Val

  final case class Signed private(contents: Address, signatory: Address) extends Value {
    def this(message: Address, signatory: Idee) = this(message, signatory.address)
  }

  sealed trait Port extends Value
  sealed trait Address extends Value

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
