package fides2024.test

import fides2024.syntax.*
import fides2024.syntax.values.*
import fides2024.syntax.polar.*
// todo could 'export' help us reduce the number of imports?

import scala.collection.concurrent
import scala.language.implicitConversions

@main def syntax(): Unit =
  given Context = new Context:
    override def prefix: String = ""
    override val names: concurrent.Map[String, Identifier] = concurrent.TrieMap.empty

  val posLoc = Inp(Channel[Bool]())
  val negLoc = Out(Channel[Bool]())
  val extractID = ExtractID(IdentifierKey())
  println(Sign(Pair(posLoc, extractID), IdentifierKey()))
  println(Sign(Pair(Identifier(), Identifier()), IdentifierKey()))
  println(Sign[Bool](posLoc, IdentifierKey()))
//  println(UnSign(negLoc, Identifier()))
  println(Sign(Wrap(posLoc), IdentifierKey()))
//  println(UnSign(Unwrap(negLoc), Identifier()))
  println(SignedMatcher(BigInt(0), Identifier(), Channel()))
//  println(UnSign(Unwrap(negLoc), Escape(Wrap(Identifier()))))
//  println(UnSign(Unwrap(negLoc), Escape(Wrap(ExtractID(IdentifierKey())))))
  println(UnSign(UnWrap(negLoc), Out(Escape(Wrap(Channel[Identifier]())))))
  println(UnSign(UnWrap(negLoc), Out(Escape(Wrap(Inp[Channel[Identifier]](Channel()))))))
