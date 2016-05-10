package syntax

final class Sign(o: => Taker[Signed]) {
  val out = o

  object contents extends In[Idee] {
    protected[Sign] var continuation: Idee => Unit = (c: Idee) => {
      signatoryPort.continuation = (s: Port) => sign(c, s)
    }
  }

  object signatoryPort extends In[Port] {
    protected[Sign] var continuation: Port => Unit = (s: Port) => {
      contents.continuation = (c: Idee) => sign(c, s)
    }
  }

  private[this] def sign(c: Idee, s: Port): Unit = out(new Signed(c, s))
}
