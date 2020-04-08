package core

package object syntax {
  type Multiset[A] = Map[A, BigInt]

  sealed trait Direction
  sealed trait Inp extends Direction
  sealed trait Out extends Direction

  sealed trait General
  sealed trait Evaluated extends General

  sealed trait Kind
  sealed trait RegularK extends Kind
  sealed trait CodeK[+S <: G, C <: D] extends Kind
  sealed trait AllK[+S <: G] extends CodeK[A, Inp with Out] with RegularK
  // CodeK[A], because normal unevaluated stuff can be used as part of a code value.

  trait Lex[+K <: N]

  type I = Inp
  type O = Out
  type E[+K] = X[K, I]
  type R[+K] = X[K, O]
  type N = Kind
  type G = General
  type A = Evaluated
  type V[+K[_]] = E[K[A]]
  type D = Direction

  type Val = V[Nothing]

//  val Mailer   = new Address
//  val Matcher  = new Address
//  val KeySmith = new Address
//  val Witness  = new Address
//  val Notary   = new Address
//  val Compiler = new Address
}
