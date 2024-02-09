package fides.test

import fides.syntax.code.*
import fides.syntax.connectors.*
import fides.syntax.control.*
import fides.syntax.identifiers.*
import fides.syntax.meta.*
import fides.syntax.processes.*
import fides.syntax.signatures.*
import fides.syntax.values.*

import java.util

def outChanExample(using Context): Code[?] =
  val channel = Channel[OutChan[Bool]]()
  // Channel[ValType] <: OutChan[Bool]
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

def unPairExample(using Context): Code[?] =
  val myChannel = Channel[WholeNumber](name = "myChannel")
  Forward(
    Paired(WholeNumber(1), Cell(False)),
    UnPair(Out(myChannel), Ignore()),
  )

def unPairExample2(using Context): Code[?] =
  val myChannel = Channel[WholeNumber](name = "myChannel")
  Forward(
    Paired(WholeNumber(1), Cell(False)), // Paired[WholeNumber, Cell[Bool]]
    MatchPair[Nothing, Nothing, WholeNumber, ValType, Nothing, Paired[WholeNumber, ValType]](Out(myChannel), Ignore()),
    // todo why can the compiler not infer these type arguments properly?
  )

def staticAndDynamicSendExample(using Context): Code[?] =
  val channel1  = Channel[Bool]()
  val channel2  = Channel[Channel[Bool]]()
  Concurrent(
    Args(
      Forward(
        inp = channel1,
        out = Out(channel2),
      ),
      Send(
        contents = True,
        recipient = Inp(channel2),
      ),
    )
  )

def unSignExample(using Context): Code[?] =
  val myKey = ChannelKey[WholeNumber]()
  val myChannel = myKey.identifier
  val channelS = Channel[Signed[WholeNumber]]()
  val channelI  = Channel[Identifier]()
  Concurrent(
    Args(
      Send(
        contents = Sign(
          contents = WholeNumber(4),
          signatory = myKey,
        ),
        recipient = channelS,
      ),
      Forward(
        inp = Inp(channelS),
        out = UnSign(
          contents = Out(myChannel),
          signatory = Out(channelI),
        ),
      )
    )
  )

def collectExample(using Context): Code[?] =
  val sourceChannel = Channel[Bool]()
  val sizeChannel = Channel[WholeNumber]()
  Collect(
    elementSource = sourceChannel,
    size = Inp(sizeChannel),
  )

def patternMeaningExample(pattern: Code[Ptrn[Atom, ValType]])(using Context): Code[?] =
  Forward[Bool](
    Inp(Channel[Bool]()),
    Match[Atom](pattern),
  )

//def loopExample(using Context): Code[?] =
//  val collectionChannel = Channel[Collected.Some[WholeNumber]]()
//  val collectionChannel2 = Channel[Collected[WholeNumber]]()
//  Forward(
//    Inp(collectionChannel),
//    UnAddElement(
//      element = Out(Channel()),
//      others = Match(
//        pattern = UnAddElement(Ignore(), Ignore()),
//        // Ptrn[Nothing, Collected.Some[T]]
//        matchedValue = Out(collectionChannel),
//      )
//    )
//  )

// todo Try to figure out why this example doesn't work even when using
//  1. Match2 and Collected2
//  2. Match3 and Collected
//def loopExample2(using Context): Code[?] =
//  val collectionChannel = Channel[Collected.Some[WholeNumber]]()
//  val collectionChannel2 = Channel[Collected[WholeNumber]]()
//  Forward(
//    Inp(collectionChannel),
//    UnAddElement(
//      element = Out(Channel()),
//      others = Match3(
//        pattern = Collected.None,
//        alternative = Out(collectionChannel),
//      )
//    )
//  )
