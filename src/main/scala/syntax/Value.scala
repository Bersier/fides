package syntax

import scala.collection.mutable

sealed trait Val
object Unit extends Val
sealed trait Value extends Val

final case class Signed private(contents: Address, signatory: Address) extends Value {
  def this(message: Address, signatory: Idee) = this(message, signatory.address)
}

sealed trait Port extends Value
sealed trait Address extends Value

final class Idee private extends Port with Address {
  def port: Port = this
  def address: Address = this
}

object Idee {
  def makeNew: Idee = {
    val result = new Idee
    NameRegister.register(result)
    result
  }

  def makeNew(name: String): Idee = {
    val result = new Idee
    NameRegister.register(result, name)
    result
  }
}

private object NameRegister {
  private[this] val map: mutable.Map[Idee, String] = mutable.Map()

  def register(id: Idee): Unit = {
    map(id) = newName()
  }

  def register(id: Idee, name: String): Unit = {
    map(id) = name
  }

  def newName(): String = {
    "name" + map.size
  }
}
