package core

package object syntax {
  type Multiset[A] = Map[A, BigInt]

  sealed trait D
  sealed trait Inp extends D
  sealed trait Out extends D
  sealed trait Val extends Inp with Out

  sealed trait N
  sealed trait RegularK extends N
  sealed trait CodeK[+K <: N, +C <: D] extends N
//  sealed trait AllK extends CodeK[AllK, Val] with RegularK // Not sure about CodeK[AllK, Val] here...

  trait L_TOP
  trait L[+K <: N, +C <: D, +T <: L_TOP] extends L_TOP

  type I[+K <: N, +T <: X_TOP] = X[K, Inp, T]
  type O[+K <: N, +T <: X_TOP] = X[K, Out, T]
  type V[+K <: N, +T <: X_TOP] = X[K, Val, T]

//  val Mailer   = new Address
//  val Matcher  = new Address
//  val KeySmith = new Address
//  val Witness  = new Address
//  val Notary   = new Address
//  val Compiler = new Address
}
