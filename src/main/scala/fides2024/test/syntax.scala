package fides2024.test

import fides2024.syntax.*

def syntax(): Unit =
  val posLoc = Inp[Bool](Identifier())
  val negLoc = Out[Bool](Identifier())
  val extractedIdentifier = ExtractIdentifier(IdentifierKey())
  println(Sign(PairTogether(posLoc, extractedIdentifier), IdentifierKey()))
  println(Sign(PairTogether(Identifier(), Identifier()), IdentifierKey()))
  println(Sign[Bool](posLoc, IdentifierKey()))
  println(Unsign[Bool](negLoc, Identifier()))
  println(Sign(Wrap(posLoc), IdentifierKey()))
  println(Unsign(Unwrap(negLoc), Identifier()))
