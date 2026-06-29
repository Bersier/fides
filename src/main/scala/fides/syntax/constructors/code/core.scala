package fides.syntax.constructors.code

import fides.syntax.util.{Identifier, launcherIdentifier, nullIdentifier}
import util.collections.extensional.{Multiset}

// -------------------------------------------------------------------------------------------------
// This file contains all the Fides syntactic code constructors.
//
// Except for Code, all the traits in this file exist only for documentation.
// -------------------------------------------------------------------------------------------------

/**
  * Parent trait
  */
sealed trait Code:
  def grammaticallyExtends: Code = throw NotImplementedError()
  def asGrammarType[T]: T = asInstanceOf[T]

/**
  * Concrete syntactic element to express a generic piece of code.
  */
final case class AbstractCode() extends Code:
  override def grammaticallyExtends: Code = AbstractCode()

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
sealed trait Polar extends Xpolar:
  def polarType: Polarized = ???
// todo we need to be careful about not making assumptions that don't always hold
//  perhaps we shouldn't use inheritance. That's still relying on a hierarchy encoded in Scala.

/**
  * Syntactic descriptions of expressions
  *
  * Documentation type; not used for any type safety
  */
private sealed trait Expression extends Polar

/**
  * Syntactic descriptions of extractors
  *
  * Documentation type; not used for any type safety
  */
sealed trait Extractor extends Polar

sealed trait Datatype extends Extractor

/**
  * Polars that are not necessarily restricted in terms of whether they can be used as expressions and extractors.
  */
sealed trait Neutral extends Expression, Extractor

/**
  * Neutrals that might be constant.
  */
sealed trait Literal extends Neutral, Datatype

/**
  * Simlar to abstractions, but not values, and generalized to the polar setting.
  */
sealed trait Bipolar extends Xpolar

/**
  * Concrete syntactic element to express a generic xpolar.
  */
final case class AbstractXpolar() extends Xpolar:
  override def grammaticallyExtends: Code = AbstractCode()

/**
  * Concrete syntactic element to express a generic polar.
  *
  * Whenever a polar is expected in a position,
  * it should not require any com capabilities that go against its polarity.
  */
final case class AbstractPolar(@Variance.Co tipe: Code) extends Polar:
  override def polarType: Polarized = tipe.asGrammarType[Polarized]
  override def grammaticallyExtends: Code = AbstractXpolar()

/**
  * Whenever a bipolar is expected in a position,
  * it should not require any com capabilities.
  *
  * @param tipe should be a Polarized of a Backward of types
  */
final case class AbstractBipolar(@Variance.Co tipe: Code) extends Bipolar:
  override def grammaticallyExtends: Code = AbstractXpolar()

//endregion - Abstract Xpolar

//region ==== Xpolar Converters ====

final case class BlockExpr(apolarBlock: Code, headExpression: Code) extends Expression:
  override def polarType: Polarized = headExpression.asGrammarType[AbstractPolar].polarType // todo restrict to Expr
  override def grammaticallyExtends: AbstractPolar = AbstractPolar(polarType)

final case class BlockXctr(apolarBlock: Code, headExtractor: Code) extends Extractor

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

final case class Apply(component: Code, input: Code) extends Neutral, Datatype

final case class Deply(component: Code, input: Code) extends Neutral, Datatype

//endregion - Xpolar Converters

//region ==== Apolars ====

final case class AbstractApolar() extends Apolar

sealed trait Location extends Apolar

final case class Constant(name: Code, value: Code) extends Location

final case class Channel(name: Code, datatype: Code) extends Location
// todo also have Link, a simpler one-to-one version? And other similar locations?
//  And maybe go back to having no explicit process for these?

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
  * @param datatype of this cell; could be an Interval when used in a [[Wildcard]]
  */
final case class Cell(name: Code, contents: Code, datatype: Code) extends Location
// todo refinements like linear?

/**
  * Behavior/Xpolar abstraction
  *
  * Kind-of plays 2 roles:
  *  1. Packages a behavior
  *  2. Abstracts connections
  *
  * [[capabilityRequirements]] are not fully determined by [[xpolar]],
  * although they have to be compatible.
  */
final case class Abstraction(name: Code, renaming: Code, capabilityRequirements: Code, xpolar: Code) extends Location

final case class EmptyCell(name: Code, datatype: Code) extends Location

final case class Atomic(body: Code) extends Apolar

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

final case class Pulse() extends Literal

final case class AbstractBool() extends Datatype

/**
  * When [[representation]] is [[None]] then this is a concrete syntactic element to express a generic Bool literal.
  */
final case class Bool(representation: Boolean) extends Literal

final case class AbstractName() extends Datatype

/**
  * Akin to names in the pi-calculus
  *
  * Names are mostly used as syntactic entities, in which case they are hard-coded. But names can also be values.
  *
  * When [[representation]] is [[None]] then this is a concrete syntactic element to express a generic name.
  */
final case class Name(representation: Identifier) extends Literal

final case class AbstractNat() extends Datatype

/**
  * When [[representation]] is [[None]] then this is a concrete syntactic element to express a generic Nat literal.
  */
final case class Nat(representation: BigInt) extends Literal

/**
  * Behavior/Xpolar abstraction reference literal
  *
  * Kind-of plays 2 roles:
  *  1. Reifies behaviors
  *  2. Abstracts connections
  *
  * Abstraction references can also be thought of and used as nominal abstraction values.
  *
  * [[capabilityRequirements]] are not determined by [[xpolar]].
  */
final case class AbstractionRef(
  name: Code, renaming: Code, capabilityRequirements: Code, xpolarType: Code,
) extends Literal

/**
  * Now also plays the role of a signed value, aka document.
  *
  * @param key a name
  */
final case class KeyedValue(key: Code, value: Code) extends Literal

/**
  * Concrete syntactic element to express a generic bag with an element type upper bound.
  */
final case class AbstractBag(refinement: Code, elementType: Code) extends Datatype

/**
  * When the elements are not all polars, then a bag can still serve a syntactic purpose,
  * namely of representing a multiset (i.e. unordered collection) of syntactic entities.
  *
  * If [[refinement]] is [[Bag.Refinement.Any]], but it's actually a record, then it's not a [[Literal]].
  * But it could still be a [[Neutral]] and a [[Datatype]]. Similarly for [[Bag.Refinement.Record]].
  */
final case class Bag(refinement: Code, elements: Multiset[Code]) extends Literal

object Bag:

  sealed trait Refinement extends Code

  final case class Any() extends Refinement
  // this is not reduced... Datatype?

  /**
    * Records are dictionaries. So they are a special type of bag.
    */
  final case class Record() extends Refinement

  /**
    * Renamings are bijections from a set of names to another. So they are a special type of record.
    */
  final case class Renaming() extends Refinement

end Bag

/**
  * The correct alpha-equivariance based on the name is built in.
  *
  * All quotes are also prequotes.
  * So [[Quote]](C) is the same as [[Prequote]](C) at the value level (but not at the syntax level).
  *
  * Quote validity is fully determined by [[code]]. So are the [[capabilityRequirements]].
  */
final case class Quote(name: Code, validity: Code, capabilityRequirements: Code, code: Code) extends Literal

object Quote:
  sealed trait Validity extends Code

  /**
    * Validity of any quote. It should not be provably ill formed.
    */
  final case class Any() extends Validity

  /**
    * Validity of a quote that is provably well formed in some sense.
    *
    * If there are no top-level unbound escapes or wildcards -
    * i.e. it doesn't require and quote capability - then it could be compiled.
    *
    * If there are no capability requirements at all, then it's a launchable quote.
    */
  final case class Valid() extends Validity
end Quote

//endregion - Constructor/Destructor Polars

//region ==== Other Reversible Polars ====

/**
  * <h2>Location connection</h2>
  * As an expression, sends a value to a fixed location.
  * As an extractor, receives a value from a fixed location.
  *
  * The value is guaranteed to arrive eventually, assuming someone listens eventually.
  * Sending is also guaranteed to be fully private.
  *
  * A message cannot be captured by any Inp that is on hold. If there is a possibility for an Inp to capture the
  * message in the future, it should not disappear. In other words, a message waits until an Inp is ready to receive it.
  * On the other hand, if it is known that there will never be any Inp for the channel, the message should eventually
  * get garbage-collected.
  *
  * <b>Syntax</b>
  *  - [[name]]: Name
  *  - [[datatype]]: Datatype
  *  - [[this]]: Polar
  *
  * <b>Semantics</b>
  * @param name of the location to which to connect for a one-time read or write
  * @param datatype of the message
  */
final case class Com(name: Code, datatype: Code) extends Neutral

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

// todo reintroduce location references

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

final case class At(key: Code, record: Code) extends Neutral, Datatype

/**
  * @param refinement needed to express certain types
  */
final case class Flatten(refinement: Code, bags: Code) extends Neutral, Datatype

/**
  * @param transformation a bipolar
  */
final case class Push(refinement: Code, bag: Code, transformation: Code) extends Neutral, Datatype
// todo add fold as well?

/**
  * Applies the given transformation to each descendent of the root of the given quote whose type is compatible,
  * and outputs the updated quote.
  *
  * @param quote to be equivariantly transformed
  * @param transformation to be applied to compatible descendents of the quote root node
  */
final case class Update(quote: Code, transformation: Code) extends Neutral

/**
  * Wraps a value into a Quoted.
  *
  * As an extractor, unwraps a value in quotes.
  *
  * @param value an expression whose value it reduces to is to be wrapped
  * @return an expression of a quote
  */
final case class Wrap(value: Code) extends Neutral
// todo bipolar?

/**
  * As an Expr, converts a [[Bag]] of code quotations to a [[Quoted]] of [[Bag]] of all the pieces of code.
  *
  * As an Xctr, extracts the arguments out of a [[Quoted]] of [[Bag]].
  */
final case class Zip(pieces: Code) extends Neutral, Datatype
// todo bipolar?

/**
  * Creates a nominal abstraction, a special case of [[AbstractionReference]].
  *
  * As an extractor, it behaves like concretion.
  */
final case class Abstract(renaming: Code, body: Code) extends Neutral, Datatype

final val NullName = Name(nullIdentifier)

//endregion - Other Reversible Polars

//region ==== Other Expression Polars ====

final case class Conjoin(conjuncts: Code) extends Expression

final case class Disjoin(disjuncts: Code) extends Expression

final case class RandomBit() extends Expression

final case class Sum(terms: Code) extends Expression

final case class Multiply(factors: Code) extends Expression

/**
  * Deterministic collision-resistant (i.e. effectively injective) function
  * from any value to a name.
  */
final case class AsName(value: Code) extends Expression

/**
  * Outputs the children of the root of the given quote, as a bag.
  */
final case class Children(quote: Code) extends Expression

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
  *  - [[recipients]]: Bag[Xctr[T]]
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
// todo Equals?

/**
  * Launches the given quote (wrapped in an abstraction) as a new process,
  * and outputs
  *  1. a renaming for how the abstraction got concretized
  *  2. abstraction refs for all abstraction locations whose names are public
  *  3. a signed value (aka document) of the code, confirming the launch
  *
  * <b>Syntax</b>
  *  - [[renaming]]: Xctr[Record[Quote[Name]]]
  *  - [[abstractionRefs]]: Xctr[Record[AbstractionRef]]
  *  - [[certificate]]: Xctr[Document[LauncherName, Quote]]
  *  - [[this]]: Xctr[Abstraction[?, Quote]]
  */
final case class Launch(renaming: Code, abstractionRefs: Code, certificate: Code) extends Extractor

//endregion - Other Extractor Polars

final val LauncherName = Name(launcherIdentifier)

/**
  * Akin to `new` in the pi-calculus
  *
  * Behaves like a nominal abstraction in quotes, so pattern matching on it respects alpha-equivariance
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

/**
  * Together with [[New]], allows the expression of parametric types.
  */
final case class Parameter(name: Code, bound: Code) extends Datatype

/**
  * Allows escaping the body of a quote.
  *
  * @param quoteName of the quote to escape
  * @param quote to insert or extract
  */
final case class Escape(quoteName: Code, quote: Code) extends Code

final case class Wildcard(quoteName: Code, grammartype: Code) extends Code

final case class Embed(mapping: Code, behavior: Code) extends Code

sealed trait Polarized extends Code
object Polarized:

  final case class Abstract() extends Polarized

  final case class Unknown () extends Polarized

  /**
    * Aka Expression
    */
  final case class Positive(tipe: Code) extends Polarized

  /**
    * Aka Extractor
    */
  final case class Negative(tipe: Code) extends Polarized

  final case class Neutral (tipe: Code) extends Polarized

  final case class Datatype(tipe: Code) extends Polarized

  final case class Literal (tipe: Code) extends Polarized

  /**
    * For all t that are subtypes of T, Literal(t) is a subtype of Interval(T).
    */
  final case class Interval(tipe: Code) extends Polarized
end Polarized

final case class Union(types: Code) extends Datatype

sealed trait Capability extends Code

object Capability:

  /**
    * Provides the right to assume that a quote with [[name]] surrounds the code.
    * This capability only makes sense for quote capability requirements.
    */
  final case class Quote(name: Code) extends Capability

  /**
    * Provides the right to assume that [[name]] is fresh (via [[New]]).
    * Provides the right to sign with [[name]].
    */
  final case class Ownership(name: Code) extends Capability

  final case class Parameter(name: Code, bound: Code) extends Capability

  /**
    * Provides some capability related to communication and locations.
    *
    * The capability requirements of a neutral are expressed as if the neutral were used in an expression position.
    *
    * Implies [[Ownership]] capability.
    */
  final case class Com(name: Code, tipe: Code, datatype: Code, locationType: Code) extends Capability

  object Com:
    enum Type extends Code:
      case Any
      case Positive
      case Negative

      /**
        * A [[Com]] capability with [[Neutral]] as type
        * provides the right to use a location in both polarities.
        */
      case Neutral

      /**
        * A [[Com]] capability with [[Location]] as type provides the right to define a location.
        * It is therefore linear. Only one location per name is allowed.
        */
      case Location
  end Com

  object Location:
    enum Type extends Code:
      case Any, Constant, Channel, Cell
  end Location
end Capability
