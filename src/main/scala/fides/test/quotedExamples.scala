package fides.test

import fides.syntax.code.Code
import fides.syntax.connectors.*
import fides.syntax.identifiers.*
import fides.syntax.meta.*
import fides.syntax.processes.*
import fides.syntax.signatures.*
import fides.syntax.values.*


def quoteExample(using Context): Code[?] =
  val channelC = Channel[Channel[Str]]()
  Quote(
    Send(
      contents = Str("Hello World!"),
      recipient = Escape(Wrap(Inp(channelC))), // Escape(Inp(channelC)) has an automatic wrap because of ValQ
    )
  )

def unQuoteExample(using Context): Code[?] =
  val channelS = Channel[Str]()
  val channelQ = Channel[Quoted[Channel[Str]]]()
  Forward(
    Quote(
      Send(
        contents = Str("Hello World!"),
        recipient = channelS,
      )
    ),
    Match(
      MatchQuote(
        Send(
          contents = MatchEscape(UnWrap(Out(channelS))),
          recipient = MatchEscape(UnWrap(Out(channelQ))), // UnWrap not needed because the channel takes Quoted, ValQ?
        )
      )
    ),
  )

/**
  * [[SignedMatcher]] can be reproduced by using [[MatchEscape]], [[MatchWrap]]  and [[MatchSign]]
  */
def signedMatcherExample(using Context): Code[?] =
  val myKey     = ChannelKey[WholeNumber]()
  val myChannel = myKey.identifier
  Spread(
    Quote(
      Escape(
        Sign(
          contents = WholeNumber(4),
          signatory = myKey,
        )
      )
    ),
    Args(
      Match(
        MatchQuote(
          SignedMatcher(
            document = MatchEscape(UnWrap(Out(myChannel))),
            signature = myChannel,
          )
        )
      ),
      Match(
        MatchQuote(
          MatchEscape(
            MatchWrap(
              MatchSign(
                contents = Out(myChannel),
                signatory = myChannel,
              )
            )
          )
        )
      ),
    )
  )

/**
  * There is a problem with [[MatchEscape]] that the type matched is not tracked then a wrong type can be used in the code
  */
def matchEscapeProblem(using Context): Code[?] =
  val myKey     = ChannelKey[WholeNumber]()
  val myChannel = myKey.identifier
  Spread(
    Quote(
      Escape(
        Sign(
          contents = WholeNumber(4),
          signatory = myKey,
        )
      )
    ),
    Args(
      Match(
        MatchQuote(
          SignedMatcher(
            document = MatchEscape(UnWrap(Out(Channel[Pulse]()))), // should fail but it doesn't since out is an Xctr
            signature = myChannel,
          )
        )
      ),
    )
  )

def matchEscapeMatcherExample(using Context): Code[?] =
  Forward(
    Quote(
      MatchEscape(True),
    ),
    Match(
      MatchQuote(
        MatchEscapeMatcher(Out(Channel[Quoted[WholeNumber]]())), // problem: it doesn't know the type for Quoted
      ),
    )
  )

def multiquoteExample(using Context): Code[?] =
  val myIntChannel = Channel[WholeNumber]()
  Quote(
    Send(
      contents = Quote(
        Add(
          QuotedEscape(Inp(myIntChannel)),
          Escape(Negate(WholeNumber(5)))
        )
      ),
      recipient = Inp(Channel())
    )
  )