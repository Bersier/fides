package syntax

import scala.concurrent.Future

final class UnpackSigned(mT: => Taker[Idee], uT: => Taker[Idee]) extends Taker[Signed] {
  val mTaker = mT
  val uTaker = uT

  override def apply(v: Signed): Unit = {
    Future(mTaker(v.contents))(null) // TODO:
    uTaker(v.signatory)
  }
}
