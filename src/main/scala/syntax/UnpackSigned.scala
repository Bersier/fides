package syntax

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

final class UnpackSigned(cT: => Taker[Idee], sT: => Taker[Idee]) extends Taker[Signed] {
  val contentsTaker = cT
  val signatureTaker = sT

  override def apply(v: Signed): Unit = {
    Future(contentsTaker(v.contents))
    Future(signatureTaker(v.signatory))
  }
}
