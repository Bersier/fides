package fides.syntax.types

sealed trait Data

final case class Pulse() extends Data

final case class Bool(representation: Boolean) extends Data

final case class Nat(representation: BigInt) extends Data

final case class Address(name: Data, datatype: Data) extends Data

final case class Entry(key: Data, value: Data) extends Data

final case class Document(signatory: Data, contents: Data) extends Data

final case class Quote(code: Data) extends Data

final case class Bag(elements: Data) extends Data

final case class Pick(options: Data) extends Data
