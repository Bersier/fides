package syntax

import commons.Utils
import semantics.Env

final class Send(o: => UnitTaker) {
  val out = o

  def address: Address = {
    if (addressVar == null) Utils.throwAlreadyUsedException(this + ".address")
    else addressVar
  }
  
  def message: Message = {
    if (messageVar == null) Utils.throwAlreadyUsedException(this + ".message")
    else messageVar
  }

  // TODO: use to decompile graph
  private[this] var a: Idee = null
  private[this] var m: Val = null

  private[this] var addressVar: Address = new Address

  private[this] var messageVar: Message = new Message

  private[this] def send(a: Idee, m: Val): Unit = {
    Env.send(a, m)
    out()
  }

  final class Address private[Send] extends In[Idee] {
    protected[Send] var continuation = (a: Idee) => {
      Send.this.a = a
      messageVar.continuation = (m: Val) => send(Send.this.a, m)
      addressVar = null
    }
  }

  final class Message private[Send] extends In[Val] {
    protected[Send] var continuation: (Val) => Unit = (m: Val) => {
      Send.this.m = m
      addressVar.continuation = (a: Idee) => send(a, Send.this.m)
      messageVar = null
    }
  }
}
