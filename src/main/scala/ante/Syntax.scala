package ante

object Syntax {

  def unused[A](used: Boolean)(a: => A): A = {
    if (used) throw new IllegalStateException(a + " already used.")
    else a
  }

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
    *
    * @param size number of incoming branches
    */
  final class Par(size: Int)(out: Unt) {
    private[this] var counter = 0

    def in = {
      val i: In = unused(counter < size)(new In)
      counter += 1
      i
    }

    final class In private[this]() extends Expr {
      def parent = Par.this
    }

  }

  // template
  final class MyPair(out: Expr) {
    private[this] var leftUsed = false
    private[this] var riteUsed = false

    def left = {
      val l = unused(leftUsed)(new Left)
      leftUsed = true
      l
    }

    def rite = {
      val r = unused(riteUsed)(new Rite)
      riteUsed = true
      r
    }

    protected abstract class Child extends Expr {
      def parent = MyPair.this
    }

    final class Left private[this] extends Child

    final class Rite private[this] extends Child
  }
}
