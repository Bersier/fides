package syntax

import commons.Utils
import semantics.Env

final class Send {

  def address: Recipient = {
    if (addressVar == null) Utils.throwAlreadyUsedException(this + ".address")
    else addressVar
  }

  def message: Message = {
    if (messageVar == null) Utils.throwAlreadyUsedException(this + ".message")
    else messageVar
  }

  // TODO: use to decompile graph
  private[this] var a: Address = null
  private[this] var m: Val = null

  private[this] var addressVar: Recipient = new Recipient

  private[this] var messageVar: Message = new Message

  private[this] def send(a: Address, m: Val): Unit = {
    Env.send(a, m)
  }

  final class Recipient private[Send] extends In[Address] {
    protected[Send] var continuation = (a: Address) => {
      Send.this.a = a
      messageVar.continuation = (m: Val) => send(Send.this.a, m)
      addressVar = null
    }
  }

  final class Message private[Send] extends In[Val] {
    protected[Send] var continuation: (Val) => Unit = (m: Val) => {
      Send.this.m = m
      addressVar.continuation = (a: Address) => send(a, Send.this.m)
      messageVar = null
    }
  }
}
