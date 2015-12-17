package ante

object Syntax {

  val x = new Par(0)(new Par(1)(new MyPair(???).left))

  sealed trait Expr {
    def in: Val => Unit = ??? // apply (not needed to model the syntax)
  }

  sealed trait Val extends Expr

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
    * Returns a multiset of all the gotten values.
    *
    * @param size number of incoming branches
    */
  final class Par(size: Int)(out: Expr) extends Expr

  // template
  final class MyPair(out: Expr) {
    val left = new Left(this)
    val rite = new Rite(this)

    // a child should be used only once (enforce?)
    protected abstract class Child(val parent: MyPair) extends Expr

    final class Left private[MyPair](parent: MyPair) extends Child(parent)

    final class Rite private[MyPair](parent: MyPair) extends Child(parent)

  }
}
