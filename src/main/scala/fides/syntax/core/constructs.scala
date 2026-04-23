package fides.syntax.core

import util.Multiset

// -------------------------------------------------------------------------------------------------
// This file contains all the Fides syntactic code constructors.
// -------------------------------------------------------------------------------------------------

sealed trait Code

//region ==== Locations ====

final case class Cell(name: Code, contents: Code) extends Code

final case class ChanRef(name: Code) extends Code

final case class CellRef(name: Code) extends Code

//endregion - Locations

//region ==== Xpolar Converters ====

final case class BlockExpr(apolarBlock: Code, HeadExpresssion: Code) extends Code

final case class BlockXctr(apolarBlock: Code, HeadExtractor: Code) extends Code

//endregion - Xpolar Converters

//region ==== Apolars ====

/**
  * Sends a value to an address.
  *
  * The value is guaranteed to arrive eventually, assuming someone listens eventually.
  * Sending is also guaranteed to be fully private.
  *
  * A message cannot be captured by any Inp that is on hold. If there is a possibility for an Inp to capture the
  * message in the future, it should not disappear. In other words, a message waits until an Inp is ready to receive it.
  * On the other hand, if it is known that there will never be any Inp for the channel, the message should eventually
  * get garbage-collected.
  *
  * @param contents the value to be sent
  * @param recipient address of the recipient
  */
final case class Send(contents: Code, recipient: Code) extends Code

/**
  * Behaviorally equivalent to an infinite number of copies of the given body
  *
  * @param body the process to be repeated
  */
final case class Repeated(body: Code) extends Code

/**
  * Composes the given processes concurrently.
  */
final case class Concurrent(processes: Code) extends Code

//endregion - Apolars

/**
  * An unordered collection of syntactic elements
  */
final case class Args(arguments: Multiset[Code]) extends Code

/**
  * Akin to names in the pi-calculus
  */
final case class Name(identifier: Identifier) extends Code

final val LauncherName = Name(launcherIdentifier)

/**
  * Akin to `new` in the pi-calculus
  *
  * @param names that are new within the scope
  * @param body the body of the scope, in which the names are available
  */
final case class New(names: Code, body: Code) extends Code
