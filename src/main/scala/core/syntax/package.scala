package core

package object syntax {
  type Multiset[A] = Map[A, BigInt]

  sealed trait Kind
  sealed trait PatternK extends I
  sealed trait RegularK extends I
  sealed trait EitherK extends PatternK with RegularK

  sealed trait General
  sealed trait Evaluated extends General

  trait Lex[+K <: I]

  type I = Kind
  type G = General
  type A = Evaluated
  type V[K] = E[K, A]

  type Pattern = Lex[PatternK]
  type Val = V[Nothing]

//  val Mailer   = new Address
//  val Matcher  = new Address
//  val KeySmith = new Address
//  val Witness  = new Address
//  val Notary   = new Address
//  val Compiler = new Address
}
