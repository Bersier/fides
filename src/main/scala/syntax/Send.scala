package syntax

import commons.Utils
import semantics.Env

final class Send(o: => UnitTaker) {
  // TODO: keep track of first received input explicitly, in order to be able to decompile the graph back
  val out = o

  def address = if (addressVar == null) Utils.unused(used = true)(this + ".address") else addressVar
  def message = if (messageVar == null) Utils.unused(used = true)(this + ".message") else messageVar

  private[this] var addressVar: In[Idee] = new In[Idee]((a: Idee) => {
    // TODO: maybe each input should have its own type?
    messageVar.continuation = (m: Value) => send(a, m)
    addressVar = null
  })

  private[this] var messageVar: In[Value] = new In[Value]((m: Value) => {
    addressVar.continuation = (a: Idee) => send(a, m)
    messageVar = null
  })

  private[this] def send(a: Idee, m: Value): Unit = {
    Env.send(a, m)
    out()
  }

  private[this] class In[-InT <: Value](var continuation: InT => Unit) extends Taker[InT] {
    def apply(in: InT): Unit = synchronized(continuation(in))
  }
}
