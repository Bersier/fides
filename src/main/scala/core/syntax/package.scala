package core

package object syntax {
  type Multiset[A] = Map[A, BigInt]

  sealed trait General
  sealed trait Evaluated extends General

  sealed trait Kind[+S <: G]
  sealed trait PatternK[+S <: G] extends Kind[S]
  sealed trait RegularK[+S <: G] extends Kind[S]
  sealed trait BothK[+S <: G] extends PatternK[S] with RegularK[S]

  trait Lex[+K <: I]

  type I = Kind[_]
  type G = General
  type A = Evaluated
  type V[+K[_]] = E[K[A]]

  type Pattern[S] = Lex[PatternK[S]]
  type Val = V[Nothing]

//  val Mailer   = new Address
//  val Matcher  = new Address
//  val KeySmith = new Address
//  val Witness  = new Address
//  val Notary   = new Address
//  val Compiler = new Address
}
