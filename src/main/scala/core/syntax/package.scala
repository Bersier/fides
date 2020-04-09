package core

package object syntax {
  type Multiset[A] = Map[A, BigInt]

  sealed trait D
  sealed trait Inp extends D
  sealed trait Out extends D

  sealed trait G
  sealed trait A extends G

  sealed trait N
  sealed trait RegularK extends N
  sealed trait CodeK extends N
  sealed trait AllK extends CodeK with RegularK
  // CodeK[A], because normal unevaluated stuff can be used as part of a code value.

  trait Lex[+K <: N]

  type I[K, T] = Loc[K, T, Inp]
  type O[K, T] = Loc[K, T, Out]
  type E[+K] = X[K, Inp]
  type R[+K] = X[K, Out]
  type V[T] = X[AllK, Inp with Out, A, T]

  type Val = V[AllK[A, Inp with Out]]

//  val Mailer   = new Address
//  val Matcher  = new Address
//  val KeySmith = new Address
//  val Witness  = new Address
//  val Notary   = new Address
//  val Compiler = new Address
}
