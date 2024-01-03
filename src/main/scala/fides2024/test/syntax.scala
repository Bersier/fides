package fides2024.test

import fides2024.syntax.*
import fides2024.syntax.values.*
import fides2024.syntax.polar.*
// todo could 'export' help us reduce the number of imports?

@main def syntax(): Unit =
  val posLoc = Inp(Channel[Bool]())
  val negLoc = Out(Channel[Bool]())
  val extractID = ExtractID(IDKey())
  println(Sign(Pair(posLoc, extractID), IDKey()))
  println(Sign(Pair(ID(), ID()), IDKey()))
  println(Sign[Bool, IDType](posLoc, IDKey()))
  println(Unsign(negLoc, ID()))
  println(Sign(Wrap(posLoc), IDKey()))
  println(Unsign(Unwrap(negLoc), ID()))
  println(SignedMatcher(BigInt(0), ID(), ID()))
  println((
    Unsign(Unwrap(negLoc), Escape(Wrap(ID()))),
    Unsign(Unwrap(negLoc), Escape(Wrap(ExtractID(IDKey())))),
    Unsign(Unwrap(negLoc), Out(Escape(Wrap(Channel[ID[?]]())))),
    // todo doesn't work. We cannot see a way to make it work with the current approach.
  ))
//  println(Location[Bool].typeTag)

// Expr[Identifier]
// Code[Expr[Quotation[Val[Identifier]]]]
// Code[Val[Identifier]]