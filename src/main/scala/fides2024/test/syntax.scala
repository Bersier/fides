package fides2024.test

import fides2024.syntax.*
import fides2024.syntax.values.*
import fides2024.syntax.polar.*
// todo could 'export' help us reduce the number of imports?

import scala.language.implicitConversions

@main def syntax(): Unit =
  val posLoc = Inp(Channel[Bool]())
  val negLoc = Out(Channel[Bool]())
  val extractID = ExtractID(IdentifierKey())
  println(Sign(Pair(posLoc, extractID), IdentifierKey()))
  println(Sign(Pair(Identifier(), Identifier()), IdentifierKey()))
  println(Sign[Bool](posLoc, IdentifierKey()))
  println(Unsign(negLoc, Identifier()))
  println(Sign(Wrap(posLoc), IdentifierKey()))
  println(Unsign(Unwrap(negLoc), Identifier()))
  println(SignedMatcher(BigInt(0), Identifier(), Channel()))
  println((
    Unsign(Unwrap(negLoc), Escape(Wrap(Identifier()))),
    Unsign(Unwrap(negLoc), Escape(Wrap(ExtractID(IdentifierKey())))),
    Unsign(Unwrap(negLoc), Out(Escape(Wrap(Channel[Identifier]())))),
    Unsign(Unwrap(negLoc), Out(Escape(Wrap(Inp[Channel[Channel[Bool]]](Channel()))))),
  ))

given [P[V <: ValType] <: Polar[V], T <: ValType, U <: ValType](using Conversion[T, U]): Conversion[P[T], P[U]] with
  def apply(v: P[T]): P[U] = ???
// todo the dumb approach is to add all the cases here:
//  try it out with only the cases needed to get the tests to run.
//  Could the cases be generated with macros?

given [T <: CodeType, U <: CodeType](using Conversion[T, U]): Conversion[Code[T], Code[U]] with
  def apply(v: Code[T]): Code[U] = ??? // todo
