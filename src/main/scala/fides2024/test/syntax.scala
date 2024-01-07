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
//  println(Unsign(negLoc, Identifier()))
  println(Sign(Wrap(posLoc), IdentifierKey()))
//  println(Unsign(Unwrap(negLoc), Identifier()))
  println(SignedMatcher(BigInt(0), Identifier(), Channel()))
//  println(Unsign(Unwrap(negLoc), Escape(Wrap(Identifier()))))
//  println(Unsign(Unwrap(negLoc), Escape(Wrap(ExtractID(IdentifierKey())))))
  println(Unsign(Unwrap(negLoc), Out(Escape(Wrap(Channel[Identifier]())))))
  println(Unsign(Unwrap(negLoc), Out(Escape(Wrap(Inp[Channel[Channel[Bool]]](Channel()))))))
