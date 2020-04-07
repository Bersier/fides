package core

package object syntax {
  type Multiset[A] = Map[A, BigInt]

  sealed trait General
  sealed trait Evaluated extends General

  sealed trait Kind[+S <: G]
  sealed trait RegularK[+S <: G] extends Kind[S]
  sealed trait PattK[+S <: G] extends Kind[S]
  sealed trait CodeK[+S <: G] extends PattK[S]
  sealed trait AllK[+S <: G] extends CodeK[A] with RegularK[S]
  // CodeK[A], because normal unevaluated stuff can be used as part of a code value.

  trait Lex[+K <: I]

  type I = Kind[_]
  type G = General
  type A = Evaluated
  type V[+K[_]] = E[K[A]]

  type Val = V[Nothing]

//  val Mailer   = new Address
//  val Matcher  = new Address
//  val KeySmith = new Address
//  val Witness  = new Address
//  val Notary   = new Address
//  val Compiler = new Address
}
