package core

package object syntax {
  type Multiset[A] = Map[A, BigInt]

  val Mailer   = new Address
  val Matcher  = new Address
  val KeySmith = new Address
  val Witness  = new Address
  val Notary   = new Address
  val Compiler = new Address
}
