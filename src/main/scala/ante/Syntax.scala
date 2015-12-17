package ante

object Syntax {

  sealed trait Expr

  /**
    * Type of Expr:s that don't return the trivial value 'Unit'.
    */
  sealed trait Unt

  class Receive(val previousOperation: Unt) extends Expr

  class Send(val message: Expr) extends Unt

  // what about higher-order use of primitive agents?
  final case class UnpackSigned(signed: Expr) {
    val content = new Content(this)
    val signature = new Signature(this)

    class Content(val parent: UnpackSigned) extends Expr

    class Signature(val parent: UnpackSigned) extends Expr
  }

  // technically, this is not required, but it clarifies when values get ignored
  final case class Ignore(ignored: Expr) extends Unt

  /**
    * Only one of the outgoing branches shall be executed,
    * without control of which one (internal choice).
    */
  final class Plus(value: Expr)

  /**
    * Will only execute once all the incoming branches are done.
    */
  final class Par(values: Set[Expr])
}
