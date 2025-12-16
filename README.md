# Fides: A Universal Interaction Language
## Disclaimer
This is a drafty draft.
## 1. Philosophy and Design Paradigm
Fides is an *interaction language*. It posits that interaction is the fundamental unit of reality, with communication and computation emerging merely as special cases. Its primary inspiration is the **pi-calculus**, inheriting concepts of concurrency, message-passing, non-determinism, ephemeral code, mobility, and the primacy of names.

* **Interaction-Universality:** Fides is designed to faithfully represent all possible games, whether cooperative, competitive, or concurrent.
* **The Fidesphere (Trust Model):** The language runtime is the *only* required Trusted Third Party. The language itself is reified into a launch primitive. It acts as a digital notary, launching quoted process code and providing certificates of exactly what code got launched. If the runtime is trusted, it can be used to bootstrap other trusted third parties, allowing arbitrary cooperation, programmatically, amongst parties that do not trust each other a priori.
* **Timelessness:** There is no global clock. Causality is established solely through communication events (happens-before relationships).

## 2. The Syntactic Topology

One important and convenient syntactic category in Fides is polars.

### 2.1 The Polarity Lattice
1. **Neutral (Invariant):** Can flow in either direction. (e.g., Data constructors like `Record`).
2. **Expression (Covariant):** A **Source**. It produces values (e.g., Reading from a channel).
3. **Extractor (Contravariant):** A **Sink**. It consumes values (e.g., Writing to a channel).
4. **Illegal (Bivariant):** A theoretical state where constraints conflict; syntactically unconstructible.

### 2.2 Syntactic Categories
* **Polars (Open Ends):** The primary connectors. A Polar is a "half-connection" waiting to be closed or extended.
* **Bipolars (Transducers):** Single-Input, Single-Output processors. They act as pipes, filters, or functions.
* **Apolars (Processes):** The unit of execution. Fully connected circuits that run concurrently in the Fidesphere.

## 3. Primitives and Composition
### 3.1 Connection Mechanisms
* **`Forward(Polar_A, Polar_B)`:** The fundamental "Cut" operation. Fuses a Source (A) and a Sink (B) into an Apolar process.
* **`Apply(Bipolar, Polar)`:** Extends a Polar by attaching a Bipolar transducer in the forward direction (Input \to Transducer \to Polar).
* **`Deply(Bipolar, Polar)`:** Extends a Polar in reverse (Polar \to Transducer \to Output).
* **`Backward(Polar_A, Polar_B)`:** Creates a Bipolar that internally pipes data between A and B, exposing their outer ends to the context.

### 3.2 Communication & Concurrency
* **`Com(Location)`:**
* *As Expression:* Receive (Source).
* *As Extractor:* Send (Sink).


* **`Send(Data_Expr, Address_Expr)`:** An Apolar for dynamic addressing. Sends `Data` to `Address` (a reified channel reference).
* **`Pick`:**
* *As Expression:* A generic race primitive. Selects the first value arriving from multiple sources.
* *As Extractor:* A demultiplexer. Non-deterministically routes one value to one of the connected sinks.


* **`Spread`:** An Extractor that broadcasts a single received value to multiple recipients.
* **`Replicate(Apolar)`:** Persists a process, spawning copies indefinitely (often replaced by recursive embedding for stateful logic).
* **`Concurrent(Apolar...)`:** Runs multiple processes in parallel.

### 3.3 Data Structures (Spatial Construction)
* **`Record`:** Named product types.
* *Constructors:* `Record`, `ExtendRecord`.
* *Spatial:* `Bundle(LocRef_A, LocRef_B)` constructs a record by pulling values from locations A and B. Conversely, acts as a Sink that pushes record fields into those locations.


* **`Bag`:** Unordered multisets.
* *Constructors:* `Bag`, `ExtendBag`.
* *Spatial:* `Collect(Location, Size)` waits for exactly `Size` items to arrive at `Location`, then bundles them. Or does the dual.


### 3.4 State & Control
* **`Cell(Name, Value)`:** An independent Apolar process holding a single value.
* **`CompareAndSwap(CellRef, Old, New)`:** An atomic expression returning the current value.
* **`Match(Extractor_A, Extractor_B)`:** An Extractor that routes incoming data to A or B based on type matching (Structural Typing).
* **`Hold`:** An expression that holds a value until a "go" signal is received.
* **`Signal`:** A Bipolar that triggers a unit signal output upon receiving any input.

## 4. The Data Model
There is a strict bijection between Values and Data Types.

* **Fundamental Types:**
* **Behaviors:** Compiled, executable code. *Functions* are simply Bipolar Behaviors (reusable transducers).
* **Quotes:** Reified code / Abstract Syntax Trees
* **Names:** Syntactic entities representing identity/capability
* **Addresses:** Reified channel references (values that can be passed)
* **Signed Values:** Produced/Verified via `Sign(Name, Polar)`

## 5. Metaprogramming
Fides has sound and complete metaprogramming.

### 5.1 The Lifecycle of Code
1. **Construction (Quote):** Code is built as a **Quote**. It is type-safe by construction.
* **`Escape`:**


2. **Transformation:**
* **`Children`:** Returns a Bag of sub-quotes (AST children).
* **`Update`:** Maps a Bipolar over children to transform the AST.
* **`Rename`:** Dynamically applies a statically specified renaming to a Behavior, Quote or Record.
* **`Zip` / `Args`:** Tools for manipulating collections of syntactic entities.


3. **Reification (Run):**
* **`Wrap`:** Wraps a value into a quote.
* **`Eval`:** Reduces a quoted expression to a value.
* **`Compile`:** Converts a quote to a behavior.
* **`Embed`:** Inserts a Behavior value into the current context. Inherits capabilities.
* **`Launch`:** Spawns a Quote as an independent process.



### 5.2 Process Control & Migration
* **`Catchable`:** Wraps a process so it can be halted and serialized back into a Quote (capturing dynamic state).
* **`Pausable` / `Mortal` / `Contingent`:** Primitives to control process liveness based on signals or boolean cells.

## 6. The Trust Architecture
### 6.1 CertificatesThe cornerstone of Fides.

* **`Launch(Quote)`:** Returns a cryptographically signed **Certificate**.
* **The Proof:** The Certificate contains the launched code (after `PublicNew` reduction). It proves exactly what logic is running.
* **Bootstrapping:** `PublicNew` creates fresh names that are effectively "published" in the Certificate, allowing other parties to discover and connect to the new process ("Service Discovery").

### 6.2 Capabilities
* **Static Resolution:** Access rights are tied to Names in the syntactic scope.
* **Capability-Closed:** To be `Launch`-ed, a Quote must not have unbound top-level escapes, or have unresolved capability requirements.
* **Private vs. Public:** `New` creates invisible internal wiring; `PublicNew` creates visible external interfaces.


## Examples

Fides does not have an official concrete syntax. Examples are written in simplified pseudo-notation.

### Part 1: The Basics (Plumbing & Composition)
We start with the "Hello World" of interaction: connecting a Producer to a Consumer.

#### Example 1.1
A simple process that generates a "Greeting" record and sends it through a channel

```scala 3
Forward(
  // SOURCE (Expression):
  // Construct a Record literal { msg = "Hello", id = 123 }.
  Record(
    msg -> "Hello", 
    id  -> 123,
  ),

  // SINK (Extractor):
  // Consumes the value by writing it to channel at name c.
  Com(ChanRef(c)),
)

```

#### Example 1.2: Bundling
In Fides, records can be built "spatially" by pulling from different locations at the same time. This is useful for gathering dependencies.

```scala 3
Forward(
  // SOURCE: The Bundle
  // Waits until it can read one value from name1 AND one from name2.
  // Creates a record: { name1 = val_from_A, name2 = val_from_B }
  Bundle(ChanRef(name1), ChanRef(name2)),
  
  // SINK: The Writer
  // Writes the resulting record to output_chan.
  Com(ChanRef(output_chan)),
)

```
