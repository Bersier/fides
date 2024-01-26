package fides.test

import fides.syntax.code.*
import fides.syntax.connectors.*
import fides.syntax.identifiers.*
import fides.syntax.meta.*
import fides.syntax.processes.*
import fides.syntax.signatures.*
import fides.syntax.values.*

import scala.collection.concurrent
import scala.language.implicitConversions

@main def syntax(): Unit =
  println("Java version: " + System.getProperty("java.version"))
  println("Scala version: " + dotty.tools.dotc.config.Properties.simpleVersionString)
  given Context = new Context:
    override def prefix: String = ""
    override val names: concurrent.Map[String, Identifier] = concurrent.TrieMap.empty

  val posLoc = Inp(Channel[Bool]())
  val negLoc = Out(Channel[Bool]())
  val extractID = ExtractIdentifier(IdentifierKey())
  println(Sign(Pair(posLoc, extractID), IdentifierKey()))
  println(Sign(Pair(Identifier(), Identifier()), IdentifierKey()))
  println(Sign[Bool](posLoc, IdentifierKey()))
  println(UnSign(negLoc, Ignore()))
  println(Sign(Wrap(posLoc), IdentifierKey()))
  println(UnSign(UnWrap(negLoc), Ignore()))
  println(SignedMatcher(Identifier(), Channel()))
  println(UnSign(UnWrap(negLoc), Escape(Quote(Ignore()))))
  println(UnSign(UnWrap(negLoc), Out(Escape(Wrap(Channel[Identifier]())))))
  println(UnSign(UnWrap(negLoc), Out(Escape(Inp[Channel[Identifier]](Channel())))))

  println:
    val channel = Channel[OutChan[Bool]]()
    Concurrent(Args(
      Forward(
        inp = Channel[ValType](),
        out = Out(channel),
      ),
      Forward(
        inp = Quoted(Out(Escape(Inp(channel)))),
        out = Ignore(),
      )
    ))

  val myCell = Cell(Integer(0), name = "myCell")
  val myChannel = Channel[Integer]()

  println(
    Concurrent(
      VarArgs(
        Forward(
          Paired(Integer(1), Cell(False)),
          UnPair(Out(myChannel), Ignore()),
        ),
        Forward(
          Add(Read(U, myCell), Inp(myChannel)),
          Write(signal = Ignore(), iD = myCell),
        ),
      )
    )
  )
