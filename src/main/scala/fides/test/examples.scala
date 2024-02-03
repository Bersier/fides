package fides.test

import fides.syntax.code.*
import fides.syntax.connectors.*
import fides.syntax.identifiers.*
import fides.syntax.meta.*
import fides.syntax.processes.*
import fides.syntax.signatures.*
import fides.syntax.values.*

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

def randomBitGeneratorExample(using Context): Code[?] =
  val randomBitChannel = Channel[Bool]()
  Repeated(
    Forward(
      RandomBit(),
      Out(randomBitChannel)
    )
  )

def serviceExample(using Context): Code[?] =
  val addressChannel = Channel[Channel[Bool]]()
  Repeated(
    Send(
      contents = RandomBit(),
      recipient = Inp(addressChannel)
    )
  )

/**
  This example simulates if-then-else
  */
def switchExample(using Context): Code[?] =
  val channel1 = Channel[Pulse]()
  val channel2 = Channel[Pulse]()
  Switch(
    input = Pulse,
    testee = True,
    cases = Args(
      Case(
        testValue = True,
        extractor = Out(channel1)
      ),
      Case(
        testValue = False,
        extractor = Out(channel2)
      )
    )
  )

def hotSwappingExample(using Context): Code[?] =
  ???

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

// todo delete
//def loopExample2(using Context): Code[?] =
//  val collectionChannel = Channel[Collected.Some[WholeNumber]]()
//  val collectionChannel2 = Channel[Collected[WholeNumber]]()
//  Forward(
//    Inp(collectionChannel),
//    UnAddElement(
//      element = Out(Channel()),
//      others = Match(
//        pattern = Collected.None,
//        alternative = Out(collectionChannel),
//      )
//    )
//  )


