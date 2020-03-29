package semantics

object Semantics {

  trait Env {
    type Address
    type Val

    def send(a: Address, m: Val): Unit

    def register[T <: Val](r: Receiver[T]): Address
  }

  trait Receiver[-Val] { // Should be a type class, ideally

    def receive(m: Val): Unit
  }
}
