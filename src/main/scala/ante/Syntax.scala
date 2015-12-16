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

  final class Plus(value: Expr) {
    // could use it either as such in each place where it could decide to send its value
    // but then it would be currently ambiguous, as currently, reuse means duplication of data
  }
}
