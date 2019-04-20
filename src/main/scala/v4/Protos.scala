package v4

import v4.Syntax.Address

object Protos {

  /**
    * Reification of the language as an agent
    */
  trait Fides

  val Mailer     = new Address // Isn't this getting silly?
  val Comparator = new Address
  val KeySmith   = new Address
  val Notary     = new Address
  val Compiler   = new Address
}
