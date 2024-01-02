package fides2024.test

import fides2024.syntax.*

@main def syntax(): Unit =
  val posLoc = Inp(Location[Bool]())
  val negLoc = Out(Location[Bool]())
  val extractedIdentifier = ExtractIdentifier(IdentifierKey())
  println(Sign(Pair(posLoc, extractedIdentifier), IdentifierKey()))
  println(Sign(Pair(Identifier(), Identifier()), IdentifierKey()))
  println(Sign[Bool](posLoc, IdentifierKey()))
  println(Unsign[Bool, Bool](negLoc, Identifier()))
  println(Sign(Wrap(posLoc), IdentifierKey()))
  println(Unsign(Unwrap(negLoc), Identifier()))
//  println(Location[Bool].typeTag)
