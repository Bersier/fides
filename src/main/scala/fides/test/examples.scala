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
  val myChannel = Channel[Integer](name = "myChannel")
  Forward(
    Paired(Integer(1), Cell(False)),
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
  val myKey = ChannelKey[Integer]()
  val myChannel = myKey.identifier
  val channelS = Channel[Signed[Integer]]()
  val channelI  = Channel[Identifier]()
  Concurrent(
    Args(
      Send(
        contents = Sign(
          contents = Integer(4),
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
  val sizeChannel = Channel[Integer]()
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
//  val collectionChannel = Channel[Collected.Some[Integer]]()
//  val collectionChannel2 = Channel[Collected[Integer]]()
//  Forward(
//    Inp(collectionChannel),
//    UnAddElement(
//      element = Out(Channel()),
//      others = Match(
//        pattern = UnAddElement(Ignore(), Ignore()),
//        matchedValue = Out(collectionChannel),
//        // todo Match doesn't refine the type based on which option is not possible in the alternative
//      )
//    )
//  )


