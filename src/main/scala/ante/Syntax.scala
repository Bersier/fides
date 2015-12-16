package ante

object Syntax {

  val x = new ReceiveSigned
  val c = x.content: ReceiveSigned#Content

  sealed trait Expr

  class Receive extends Expr {
    val content = new Content

    class Content extends Expr

  }

  // directly decomposes the message, preventing reuse of it (bad)
  class ReceiveSigned extends Receive {
    val signature = new Signature

    class Signature extends Expr

  }

  class Send() extends Expr {

  }

}
