package syntax

import commons.Utils
import semantics.Env

final class Send(o: => UnitTaker) {
  val out = o

  def address: Address = {
    if (addressVar == null) Utils.thowAlreadyUsedException(this + ".address")
    else addressVar
  }
  
  def message: Message = {
    if (messageVar == null) Utils.thowAlreadyUsedException(this + ".message")
    else messageVar
  }

  // TODO: use to decompile graph
  private[this] var a: Idee = null
  private[this] var m: Value = null

  private[this] var addressVar: Address = new Address

  private[this] var messageVar: Message = new Message

  private[this] def send(a: Idee, m: Value): Unit = {
    Env.send(a, m)
    out()
  }

  final class Address private[Send] extends In[Idee] {
    protected[Send] var continuation = (a: Idee) => {
      Send.this.a = a
      messageVar.continuation = (m: Value) => send(Send.this.a, m)
      addressVar = null
    }
  }

  final class Message private[Send] extends In[Value] {
    protected[Send] var continuation: (Value) => Unit = (m: Value) => {
      Send.this.m = m
      addressVar.continuation = (a: Idee) => send(a, Send.this.m)
      messageVar = null
    }
  }
}
