package fides.test

import fides.syntax.code.*
import fides.syntax.connectors.*
import fides.syntax.identifiers.*
import fides.syntax.meta.*
import fides.syntax.signatures.*
import fides.syntax.values.*

import scala.language.implicitConversions
import scala.util.Properties

// todo syntax examples should go into test.syntax package
@main def syntax(): Unit =
  println("Java version: " + System.getProperty("java.version"))
  println("Scala version: " + dotty.tools.dotc.config.Properties.simpleVersionString)
  println("Scala compiler version: " + Properties.ScalaCompilerVersion)
  println("Scala library version: " + Properties.releaseVersion)
  given Context = new Context:
    override def prefix: String = ""

  val posLoc = Inp(InpChan[Bool]())
  val negLoc = Out(OutChan[Bool]())
  val extractID = ExtractIdentifier(IdentifierKey())
  println(Sign(Pair(posLoc, extractID), IdentifierKey()))
  println(Sign(Pair(Identifier(), Identifier()), IdentifierKey()))
  println(Sign[Bool](posLoc, IdentifierKey()))
  println(UnSign(negLoc, Ignore()))
  println(Sign(Wrap(posLoc), IdentifierKey()))
  println(UnSign(UnWrap(negLoc), Ignore()))
  println(SignedMatcher(Identifier(), OutChan()))
  println(UnSign(UnWrap(negLoc), Escape(Quote(Ignore()))))
  println(UnSign(UnWrap(negLoc), Out(Escape(Wrap(OutChan[Identifier]())))))
  println(UnSign(UnWrap(negLoc), Out(Escape(Wrap(Inp[OutChan[Identifier]](Channel()))))))




