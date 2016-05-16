package syntax

import commons.Utils

final class UnpackSigned(cT: => Taker[Idee], sT: => Taker[Idee]) extends Taker[Signed] {
  val contentsTaker = cT
  val signatureTaker = sT

  override def apply(v: Signed): Unit = {
    Utils.concurrently(contentsTaker(v.contents), signatureTaker(v.signatory))
  }
}
