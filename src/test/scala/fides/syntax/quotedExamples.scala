package fides.syntax

import fides.syntax.code.Code
import fides.syntax.connectors.*
import fides.syntax.identifiers.*
import fides.syntax.meta.*
import fides.syntax.processes.*
import fides.syntax.signatures.*
import fides.syntax.values.*


def quoteExample(using Context): Code[?] =
  val channelC = InpChan[OutChan[Str]]()
  Quote(
    Send(
      contents = Str("Hello World!"),
      recipient = Escape(Wrap(Inp(channelC))), // Escape(Inp(channelC)) has an automatic wrap because of Val
    )
  )

def unQuoteExample(using Context): Code[?] =
  val channelS = OutChan[Str]()
  val channelQ = OutChan[Quoted[OutChan[Str]]]()
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
          recipient = MatchEscape(UnWrap(Out(channelQ))), // UnWrap not needed because the channel takes Quoted, Val?
        )
      )
    ),
  )

/**
  * [[SignedMatcher]], which is not part of Fides anymore,
  * can be reproduced by using [[MatchEscape]], [[MatchWrap]] and [[MatchSign]].
  */
def signedMatcherExample(using Context): Code[?] =
  val myKey     = ChannelKey[WholeNumber]()
  val myChannel = myKey.identifier
  Spread(
    Quote(
      Escape(Wrap(
        Sign(
          document = WholeNumber(4),
          signatory = myKey,
        ),
      ))
    ),
    Args(
//      Match(
//        MatchQuote(
//          SignedMatcher(
//            document = MatchEscape(UnWrap(Out(myChannel))),
//            signature = myChannel,
//          )
//        )
//      ),
      Match(
        MatchQuote(
          MatchEscape(
            MatchWrap(
              MatchSign(
                document = Out(myChannel),
                signature = myChannel,
              )
            )
          )
        )
      ),
    )
  )

// todo modify code to use something different from SignedMatcher, which got removed.
/**
  * There is a problem with [[MatchEscape]]: the type matched is not tracked, so a wrong type can be used in the code.
  */
def matchEscapeProblem(using Context): Code[?] =
  val myKey     = ChannelKey[WholeNumber]()
  val myChannel = myKey.identifier
  Spread(
    Quote(
      Escape(Wrap(
        Sign(
          document = WholeNumber(4),
          signatory = myKey,
        ),
      ))
    ),
    Args(
//      Match(
//        MatchQuote(
//          SignedMatcher(
//            document = MatchEscape(UnWrap(Out(OutChan[Pulse]()))), // should fail, but doesn't because of lossy typing
//            signature = myChannel,
//          )
//        )
//      ),
    )
  )

def matchEscapeMatcherExample(using Context): Code[?] =
  Forward(
    Quote(
      MatchEscape(Quoted(True)),
    ),
    Match(
      MatchQuote(
        MatchEscapeMatcher(Out(OutChan[Quoted[WholeNumber]]())), // problem: it doesn't know the type for Quoted
      ),
    )
  )

def multiquoteExample(using Context): Code[?] =
  val myIntChannel = InpChan[WholeNumber]()
  Quote(
    Send(
      contents = Quote(
        Add(Args(
          QuotedEscape(Wrap(Inp(myIntChannel))),
          Escape(Wrap(Negate(WholeNumber(5)))),
        ))
      ),
      recipient = Inp(InpChan())
    )
  )
