package syntax

import semantics.Env

final class Receive[-InT <: Value](o: => Taker[InT]) extends Taker[Port] {
  // TODO: could make input explicit even when there is only one as in that case
  val out = o

  private[this] var port: Port = null

  def apply(port: Port): Unit = {
    this.port = port
    Env.addReceiver(this)
  }

  def receive(value: InT): Unit = {
    out(value)
  }
}
