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

def writeToCellExample(using Context): Code[?] =
  val myCell    = Cell(Integer(0), name = "myCell")
  Forward(
    Add(Read(Pulse, myCell), Integer(10)),
    Write(signal = Ignore(), iD = myCell),
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
        message = True,
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
        message = Sign(
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

def quoteExample(using Context): Code[?] =
  val channelC = Channel[Channel[Str]]()
  Quote(
    Send(
      message = Str("Hello World!"),
      recipient = Escape(Wrap(Inp(channelC))), // Escape(Inp(channelC)) has an automatic wrap because of ValQ
    )
  )

def unQuoteExample(using Context): Code[?] =
  val channelS = Channel[Str]()
  val channelQ = Channel[Quoted[Channel[Str]]]()
  Forward(
    inp = Quote(
      Send(
        message = Str("Hello World!"),
        recipient = channelS,
      )
    ),
    out = Match(
      pattern = MatchQuote(
        Send(
          message = Escape(UnWrap(Out(channelS))),
          recipient = Escape(UnWrap(Out(channelQ))), // UnWrap not needed because the channel takes Quoted, ValQ?
        )
      ),
      signal = Ignore(),
      alternative = Ignore()
    ),
  )

def signedMatcherExample(using Context): Code[?] =
  val myKey     = ChannelKey[Integer]()
  val myChannel = myKey.identifier
  val signalChannel = Channel[Pulse]()
  Spread(
    value = Quote(
      Escape(
        Sign(
          contents = Integer(4),
          signatory = myKey,
        )
      )
    ),
    recipients = Args(
      Match(
        pattern = MatchQuote(
          SignedMatcher(
            level = Integer(0),
            document = Escape(UnWrap(Out(myChannel))),
            signature = myChannel,
          )
        ),
        signal = Out(signalChannel),
        alternative = Ignore()
      ),
      Match(
        pattern = MatchQuote(
          Escape(
            MatchWrap(
              MatchSign(
                contents = Out(myChannel),
                signatory = myChannel,
              )
            )
          )
        ),
        signal = Out(signalChannel),
        alternative = Ignore()
      ),  
    )
  )


def escapeMatcher(using Context): Code[?] = ???