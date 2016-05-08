package syntax

sealed trait Value

final case class Signed private(contents: Idee, signatory: Idee) extends Value {
  def this(message: Idee, signatory: Port) = this(message, signatory.idee)
}

final case class Port private(idee: Idee) extends Value {
  def this() = this(new Idee)

  def this(name: String) = this(new NamedIdee(name))
}

object Port {
  def apply(name: String): Port = new Port(name)
}

sealed class Idee extends Value

final class NamedIdee(name: String) extends Idee {
  def this() = this(NamedIdee.newName())

  override val toString = name
}

private object NamedIdee {
  private[this] var counter: BigInt = 0

  def newName(): String = {
    counter += 1
    "name" + counter
  }
}

