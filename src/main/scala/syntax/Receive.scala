//package syntax
//
//import semantics.Env
//
//final class Receive[-InT <: Val](val uponSetup: Taker[Unit], val uponReception: Taker[InT])
//  extends Taker[Port] {
//  // TODO: could make input explicit even when there is only one as in that case
//
//  private[this] var port: Port = null
//
//  def apply(port: Port): Unit = {
//    this.port = port
//    Env.addReceiver(this)
//    uponSetup()
//  }
//
//  def receive(value: InT): Unit = {
//    uponReception(value)
//  }
//}
