package syntax

/**
  * This class just serves as an example, but is not part of the syntax.
  * Should be deleted eventually.
  */
final class MyPair(val out: ExprTaker) {
  private[this] var leftUsed = false
  private[this] var riteUsed = false

  def left = {
    val l = Utils.unused(leftUsed)(new Left)
    leftUsed = true
    l
  }

  def rite = {
    val r = Utils.unused(riteUsed)(new Rite)
    riteUsed = true
    r
  }

  protected abstract class Child extends ExprTaker {
    def parent = MyPair.this
  }

  final class Left private[MyPair] extends Child
  final class Rite private[MyPair] extends Child
}
