package fides.syntax.core

import util.Multiset

// -------------------------------------------------------------------------------------------------
// This file contains all the Fides syntactic code constructors.
// -------------------------------------------------------------------------------------------------

sealed trait Code

//region ==== Locations ====

final case class Cell(name: Code, contents: Code) extends Code

/**
  * Channel location reference
  */
final case class ChanRef(name: Code, datatype: Code) extends Code

/**
  * Cell location reference
  */
final case class CellRef(name: Code, datatype: Code) extends Code

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

final case class DivMod(dividend: Code, divisor: Code, quotient: Code, remainder: Code) extends Code

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

/**
  * @param awake a Boolean cell whose value indicates whether the process is running (True) or paused (False)
  */
final case class Pausable(awake: Code, process: Code) extends Code

/**
   * Delays [[process]] until [[signal]] is received.
   *
   * Executes the body only if the received signal is [[True]].
   * If [[False]], the body is discarded without getting executed.
  */
final case class Possible(signal: Code, process: Code) extends Code

/**
  * A killable process; apolar
  *
  * Upon reception of a pulse, the body's execution is stopped.
  *
  * @param signal when it arrives, kills the process
  * @param process the process that can be stopped; apolar
  */
final case class Mortal(signal: Code, process: Code) extends Code

/**
  * @param signal to catch the process
  * @param process that can be caught
  * @param quote an extractor for the quote of the caught process state
  */
final case class Catchable(signal: Code, process: Code, quote: Code) extends Code

/**
  * All the names used by [[contained]] are effectively new, providing isolation.
  *
  * @param monitor process, not isolated from the outside
  * @param bridgeNames new names that are accessible to both [[monitor]] and [[contained]], allowing them to interact
  * @param contained isolated process
  */
final case class Sandboxed(monitor: Code, bridgeNames: Code, contained: Code) extends Code

//endregion - Apolars

//region ==== Constructor/Destructor Polars ====

// Nullary structors

final case class Pulse() extends Code

final case class Bool(representation: Boolean) extends Code

enum Command extends Code:
  case Start, Pause, Kill

final case class Nat(representation: BigInt) extends Code

final case class Address(name: Code, datatype: Code) extends Code

// Unary structors

/**
  * @param key a name
  */
final case class Entry(key: Code, value: Code) extends Code

final case class Document(signatory: Code, contents: Code) extends Code

final case class Quote(code: Code) extends Code

// Multiset structors

final case class Bag(elements: Code) extends Code

final case class Pick(options: Code) extends Code

//endregion - Constructor/Destructor Polars

//region ==== Other Reversible Polars ====

/**
  * @param keys a multiset of location references
  */
final case class BundleG(keys: Code) extends Code

/**
  * @param keys a multiset of location references
  */
final case class SwitchG(keys: Code) extends Code

/**
  * @param channel a channel reference
  * @param size the number of elements to collect
  */
final case class CollectG(channel: Code, size: Code) extends Code

final case class NegateG(bool: Code) extends Code

final case class SwapG(name1: Code, name2: Code, target: Code) extends Code

/**
  * As an Expr, converts a [[Bag]] of code quotations to a [[Quoted]] of [[Args]] of all the pieces of code.
  *
  * As an Xctr, extracts the arguments out of a [[Quoted]] of [[Args]].
  */
final case class Zip(pieces: Code) extends Code

//endregion - Other Reversible Polars

//region ==== Other Expression Polars ====

final case class Conjoin(conjuncts: Code) extends Code

final case class Disjoin(disjuncts: Code) extends Code

final case class RandomBit() extends Code

final case class Sum(terms: Code) extends Code

final case class Multiply(factors: Code) extends Code

final case class Merge(bags: Code) extends Code

/**
  * Wraps a value into a Quoted.
  *
  * @param value an expression whose value it reduces to is to be wrapped
  * @return an expression of a quote
  */
final case class Wrap(value: Code) extends Code

/**
  * Evaluates a quoted expression.
  *
  * @param value a quote of an expression
  * @return an expression that evaluates like the expression in quotes
  */
final case class Eval(value: Code) extends Code

/**
  * Replaces all the names in the quote by fresh names. Also removes all shadowing.
  */
final case class Freshen(quote: Code) extends Code

/**
  * Applies the given transformation to each child of the root of the given quote whose type is compatible,
  * and outputs the updated quote.
  *
  * @param quote to be equivariantly transformed
  * @param transformation to be applied to compatible children of the quote root node
  */
final case class Update(quote: Code, transformation: Code) extends Code

/**
  * Outputs the children of the root of the given quote, as a bag.
  */
final case class Children(quote: Code) extends Code

/**
  * Launches [[quote]] as a new process, and outputs a signed value (aka document) of the code, confirming the launch.
  */
final case class Launch(quote: Code) extends Code
// todo take quote wrapped in New? To expose/publish some names the launched process nevertheless owns?

//endregion - Other Expression Polars

//region ==== Other Extractor Polars ====

final case class Inspect(signature: Code, payload: Code)

//endregion - Other Extractor Polars

/**
  * Akin to names in the pi-calculus
  */
final case class Name(representation: Identifier) extends Code

final val LauncherName = Name(launcherIdentifier)

/**
  * An unordered collection of syntactic elements
  */
final case class Args(arguments: Multiset[Code]) extends Code

/**
  * Akin to `new` in the pi-calculus
  *
  * @param names that are new within the scope
  * @param body the body of the scope, in which the names are available
  */
final case class New(names: Code, body: Code) extends Code

/**
  * An annotated piece of code. The annotation does not change the semantics of the code.
  * It acts as a structured comment.
  *
  * If the annotation is bound to a quote, then it belongs to that quote.
  *
  * @param quoteName the name of the quote to which this quote is bound
  * @param code the annotated code
  * @param annotation the annotation; a value
  */
final case class Annotated(quoteName: Code, code: Code, annotation: Code) extends Code

/**
  * Allows escaping the body of a quote.
  *
  * @param name of the quote to escape
  * @param quote to insert
  */
final case class Escape(name: Code, quote: Code) extends Code
