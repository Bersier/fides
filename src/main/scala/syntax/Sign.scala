package syntax

final class Sign(o: => Taker[Signed]) {
  val out = o

  object contents extends In[Address] {
    protected[Sign] var continuation: Address => Unit = (c: Address) => {
      signatory.continuation = (s: Idee) => sign(c, s)
    }
  }

  object signatory extends In[Idee] {
    protected[Sign] var continuation: Idee => Unit = (s: Idee) => {
      contents.continuation = (c: Address) => sign(c, s)
    }
  }

  private[this] def sign(c: Address, s: Idee): Unit = out(new Signed(c, s))
}
