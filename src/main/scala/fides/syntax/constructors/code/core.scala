package fides.syntax.constructors.code

import fides.syntax.util.{Identifier, launcherIdentifier}
import util.collections.extensional.{Multiset}

// -------------------------------------------------------------------------------------------------
// This file contains all the Fides syntactic code constructors.
// -------------------------------------------------------------------------------------------------

/**
  * Parent trait
  */
sealed trait Code

/**
  * Concrete syntactic element to express a generic piece of code.
  */
final case class AbstractCode() extends Code

//region ==== Abstract Xpolar ====

/**
  * Xpolars are syntactic entities that represent behaviors.
  *
  * Documentation type; not used for any type safety
  */
sealed trait Xpolar extends Code

/**
  * Syntactic descriptions of processes
  *
  * Documentation type; not used for any type safety
  */
sealed trait Apolar extends Xpolar

/**
  * Syntactic descriptions of polars, which represent specialized processes with one distinguished xput,
  * so that syntactic composition is as easy as for expressions in other languages
  *
  * Documentation type; not used for any type safety
  */
sealed trait Polar extends Xpolar

/**
  * Syntactic descriptions of expressions
  *
  * Documentation type; not used for any type safety
  */
sealed trait Expression extends Polar

/**
  * Syntactic descriptions of extractors
  *
  * Documentation type; not used for any type safety
  */
sealed trait Extractor extends Polar

/**
  * Polars that are not necessarily restricted in terms of whether they can be used as expressions and extractors.
  */
sealed trait Neutral extends Expression, Extractor

/**
  * Neutrals that might be constant.
  */
sealed trait Constant extends Neutral

/**
  * Simlar to abstractions, but not values, and generalized to the polar setting.
  */
sealed trait Bipolar extends Xpolar

/**
  * Concrete syntactic element to express a generic xpolar.
  */
final case class AbstractXpolar() extends Polar

/**
  * Concrete syntactic element to express a generic polar.
  */
final case class AbstractPolar() extends Polar

/**
  * Concrete syntactic element to express a generic expression polar.
  *
  * @param datatype of the polar
  */
final case class AbstractExpression(datatype: Code) extends Expression

/**
  * Concrete syntactic element to express a generic extractor polar.
  *
  * @param datatype of the polar
  */
final case class AbstractExtractor(datatype: Code) extends Extractor

/**
  * Concrete syntactic element to express a generic reversible polar.
  *
  * @param datatype of the polar
  */
final case class AbstractReversiblePolar(datatype: Code) extends Neutral

final case class AbstractBipolar() extends Bipolar

final case class AbstractExpressionBipolar(inputDatatype: Code, outputDatatype: Code) extends Bipolar

final case class AbstractExtractorBipolar(inputDatatype: Code, outputDatatype: Code) extends Bipolar

final case class AbstractReversibleBipolar(inputDatatype: Code, outputDatatype: Code) extends Bipolar

//endregion - Abstract Xpolar

//region ==== Location References ====

sealed trait LocRef extends Code

final case class AbstractLocRef(name: Code, datatype: Code) extends LocRef

/**
  * Channel location reference
  */
final case class ChanRef(name: Code, datatype: Code) extends LocRef

/**
  * Cell location reference
  */
final case class CellRef(name: Code, datatype: Code) extends LocRef

//endregion - Location References

//region ==== Xpolar Converters ====

final case class BlockExpr(apolarBlock: Code, HeadExpresssion: Code) extends Expression

final case class BlockXctr(apolarBlock: Code, HeadExtractor: Code) extends Extractor

/**
  * A hard-coded connection between one input and one output
  *
  * <b>Syntax</b>
  *  - [[inp]]: Expr
  *  - [[out]]: Xctr
  *  - [[this]]: Apolar
  */
final case class Forward(inp: Code, out: Code) extends Apolar

/**
  * Dual of Forward. The connection between [[inp]] and [[out]] is instead achieved via variables.
  */
final case class Backward(inp: Code, out: Code) extends Bipolar

final case class Apply(component: Code, input: Code) extends Neutral

final case class Deply(component: Code, input: Code) extends Neutral

//endregion - Xpolar Converters

//region ==== Apolars ====

final case class AbstractApolar() extends Apolar

/**
  * <h2>Memory cell process</h2>
  * Stores a single value. Can be updated, and can be used for synchronization.
  *
  * <b>Syntax</b>
  *  - [[name]]: Name
  *  - [[contents]]: Ntrl
  *  - [[this]]: Apolar
  *
  * <b>Semantics</b>
  * @param name used to refer to this cell
  * @param contents of this cell
  */
final case class Cell(name: Code, contents: Code) extends Apolar

final case class Atomic(body: Code) extends Apolar

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
final case class Send(contents: Code, recipient: Code) extends Apolar

final case class DivMod(dividend: Code, divisor: Code, quotient: Code, remainder: Code) extends Apolar

/**
  * Behaviorally equivalent to an infinite number of copies of the given body
  *
  * @param body the process to be repeated
  */
final case class Repeated(body: Code) extends Apolar

/**
  * Composes the given processes concurrently.
  */
final case class Concurrent(processes: Code) extends Apolar

/**
  * @param awake a Boolean cell whose value indicates whether the process is running (True) or paused (False)
  */
final case class Pausable(awake: Code, process: Code) extends Apolar

/**
   * Delays [[process]] until [[signal]] is received.
   *
   * Executes the body only if the received signal is [[True]].
   * If [[False]], the body is discarded without getting executed.
  */
final case class Possible(signal: Code, process: Code) extends Apolar

/**
  * A killable process; apolar
  *
  * Upon reception of a pulse, the body's execution is stopped.
  *
  * @param signal when it arrives, kills the process
  * @param process the process that can be stopped; apolar
  */
final case class Mortal(signal: Code, process: Code) extends Apolar

/**
  * @param signal to catch the process
  * @param process that can be caught
  * @param quote an extractor for the quote of the caught process state
  */
final case class Catchable(signal: Code, process: Code, quote: Code) extends Apolar

/**
  * All the names used by [[contained]] are effectively new, providing isolation.
  *
  * @param monitor process, not isolated from the outside
  * @param bridgeNames new names that are accessible to both [[monitor]] and [[contained]], allowing them to interact
  * @param contained isolated process
  */
final case class Sandboxed(monitor: Code, bridgeNames: Code, contained: Code) extends Apolar

//endregion - Apolars

//region ==== Constructor/Destructor Polars ====

// Nullary structors

final case class Pulse() extends Constant

/**
  * When [[representation]] is [[None]] then this is a concrete syntactic element to express a generic Bool constant.
  */
final case class Bool(representation: Option[Boolean]) extends Constant

/**
  * Akin to names in the pi-calculus
  *
  * Names are mostly used as syntactic entities, in which case they are constants. But names can also be values.
  *
  * When [[representation]] is [[None]] then this is a concrete syntactic element to express a generic name.
  */
final case class Name(representation: Option[Identifier]) extends Constant

/**
  * When [[representation]] is [[None]] then this is a concrete syntactic element to express a generic Nat constant.
  */
final case class Nat(representation: Option[BigInt]) extends Constant

final case class Address(name: Code, datatype: Code) extends Constant

// Unary structors

/**
  * @param key a name
  */
final case class Entry(key: Code, value: Code) extends Constant

final case class Document(signatory: Code, contents: Code) extends Constant

// Variadic structors

final case class Bag(elements: Code) extends Constant

/**
  * Records are dictionaries. So they are a special type of bag.
  *
  * So [[Record]](C) is the same as [[Bag]](C) at the value level (but not at the syntax level).
  */
final case class Record(elements: Code) extends Constant

/**
  * A quote that could be compiled.
  *
  * Typewise, it's still a quote.
  * So [[CompilableQuote]](C) is the same as [[Quote]](C) at the value level (but not at the syntax level).
  *
  * At the top level, it should
  *  1. have no unbound escapes
  *  2. follow the usual syntax (unlike extractor quotes)
  *  3. have no abstract syntactic constructors
  *
  * If there are no capability requirements, then it's a launchable quote.
  */
final case class CompilableQuote(name: Code, capabilityRequirements: Code, code: Code) extends Constant

/**
  * The correct alpha-equivariance based on the name is built in.
  *
  * All quotes are also prequotes.
  * So [[Quote]](C) is the same as [[Prequote]](C) at the value level (but not at the syntax level).
  *
  * Also, autowrapping means that [[Quote]](C) is the same as C, when C is a whitebox constant/value
  * (which is all values, except for abstractions of non-constants).
  */
final case class Quote(name: Code, capabilityRequirements: Code, code: Code) extends Constant

/**
  * The correct alpha-equivariance based on the name is built in.
  */
final case class Prequote(name: Code, capabilityRequirements: Code, code: Code) extends Constant

//endregion - Constructor/Destructor Polars

//region ==== Other Reversible Polars ====

/**
  * <h2>Location connection</h2>
  * As an expression, sends a value to a fixed location.
  * As an extractor, receives a value from a fixed location.
  *
  * <b>Syntax</b>
  *  - [[reference]]: LocRef
  *  - [[this]]: Polar
  *
  * <b>Semantics</b>
  * @param reference to which to connect for a one-time read or write
  */
final case class Com(reference: Code) extends Neutral

/**
  * As an expression, forwards one of the inputs.
  * Is guaranteed to forward a value if any of the inputs yields a value.
  * Another way to think about this is that
  * it forwards the value of the expression that "first" reduces to a value.
  *
  * As an extractor, behaves like internal choice:
  * non-deterministically forwards the input to one of the outputs.
  *
  * Dual of [[Bag]], in the sense that [[Bag]] constructs or destructs an untagged product,
  * while [[Pick]] constructs or destructs an untagged sum.
  *
  * <b>Syntax</b>
  *  - [[options]]: NonEmptyArgs[T]
  *  - [[this]]: Polar[T]
  */
final case class Pick(options: Code) extends Neutral

/**
  * As an expression, forwards the inputted value once signalled to do so.
  *
  * As an extractor, notifies one listener of the arrival of the value.
  *
  * <b>Syntax</b>
  *  - [[signal]]: Expr[Pulse]
  *  - [[value]]: Expr[T]
  *  - [[this]]: Expr[T]
  */
final case class Hold(signal: Code, value: Code) extends Neutral

/**
  * @param keys a multiset of location references
  */
final case class Bundle(keys: Code) extends Neutral

/**
  * @param keys a multiset of location references
  */
final case class Switch(keys: Code) extends Neutral

/**
  * @param channel a channel reference
  * @param size the number of elements to collect
  */
final case class Collect(channel: Code, size: Code) extends Neutral

final case class Negate(bool: Code) extends Neutral

final case class Flatten(bags: Code) extends Neutral

/**
  * As an Expr, converts a [[Bag]] of code quotations to a [[Quoted]] of [[Args]] of all the pieces of code.
  *
  * As an Xctr, extracts the arguments out of a [[Quoted]] of [[Args]].
  */
final case class Zip(pieces: Code) extends Neutral

//endregion - Other Reversible Polars

//region ==== Other Expression Polars ====

final case class Conjoin(conjuncts: Code) extends Expression

final case class Disjoin(disjuncts: Code) extends Expression

final case class RandomBit() extends Expression

final case class Sum(terms: Code) extends Expression

final case class Multiply(factors: Code) extends Expression

/**
  * Deterministic collision-resistant (i.e. effectively injective) mapping
  * from any value to a name (represented as a name quote).
  */
final case class AsName(value: Code) extends Expression

final case class Push(bag: Code, transformation: Code) extends Expression

final case class At(key: Code, record: Code) extends Expression

/**
  * Behavior/Xpolar abstraction literal.
  *
  * Abstractions are black-box, so they cannot be inspected. Note however that if the xpolar is a constant,
  * then that constant will be returned, which itself can be inspected.
  *
  * Abstraction values can also be though of and used as nominal abstraction values.
  */
final case class Abstraction(mapping: Code, capabilityRequirements: Code, code: Code) extends Expression

/**
  * Compiles an xpolar quote to a behavior.
  */
final case class Compile(mapping: Code, xpolarQuote: Code) extends Expression

/**
  * Applies the given transformation to each descendent of the root of the given quote whose type is compatible,
  * and outputs the updated quote.
  *
  * @param quote to be equivariantly transformed
  * @param transformation to be applied to compatible descendents of the quote root node
  */
final case class Update(quote: Code, transformation: Code) extends Expression

/**
  * Outputs the children of the root of the given quote, as a bag.
  */
final case class Children(quote: Code) extends Expression

final case class Validate(prequote: Code) extends Expression

//endregion - Other Expression Polars

//region ==== Other Extractor Polars ====

/**
  * Spreads a value to zero or more recipients.
  *
  * Nonlinear primitive
  *
  * [[Spread]](`<no-recipient>`) is equivalent to Ignore/Sink/Forget/Discard/Drop.
  *
  * <b>Syntax</b>
  *  - [[recipients]]: Args[Xctr[T]]
  *  - [[this]]: Xctr[T]
  */
final case class Spread(recipients: Code) extends Extractor

/**
  * <h2>Match statement</h2>
  *
  * <b>Syntax</b>
  *  - [[pattern]]: Xctr
  *  - [[alternative]]: Xctr
  *  - [[this]]: Xctr
  *
  * <b>Semantics</b>
  * @param pattern executes if the singleton type of the inputted value is a subtype of its type
  * @param alternative defaults to this otherwise
  */
final case class Match(pattern: Code, alternative: Code) extends Extractor

final case class Inspect(signature: Code, payload: Code) extends Extractor

/**
  * Launches the given quote (wrapped in an abstraction) as a new process,
  * and outputs a signed value (aka document) of the code, confirming the launch,
  * as well as a mapping for how the abstraction got concretized.
  *
  * <b>Syntax</b>
  *  - [[mapping]]: Xctr[Record[Quote[Name]]]
  *  - [[certificate]]: Xctr[Document[LauncherName, Quote]]
  *  - [[this]]: Xctr[Abstraction[?, Quote]]
  */
final case class Launch(mapping: Code, certificate: Code) extends Extractor

//endregion - Other Extractor Polars

final val LauncherName = Name(Some(launcherIdentifier))

/**
  * Concrete syntactic element to express a generic bag.
  */
final case class AbstractArgs(arguments: Multiset[Code], restElementType: Code) extends Constant

/**
  * An unordered collection of syntactic elements
  */
final case class Args(arguments: Multiset[Code]) extends Code

/**
  * Akin to `new` in the pi-calculus
  *
  * Behaves like a (biased) nominal abstraction in quotes, so pattern matching on it respects alpha-equivariance
  *
  * @param names that are new within the scope
  * @param body the body of the scope, in which the names are available
  */
final case class New(names: Code, body: Code) extends Xpolar

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

final case class Type(witness: Option[Code]) extends Code

/**
  * Allows escaping the body of a quote.
  *
  * @param name of the quote to escape
  * @param quote to insert
  */
final case class Escape(name: Code, quote: Code) extends Code

final case class Embed(mapping: Code, behavior: Code) extends Code
