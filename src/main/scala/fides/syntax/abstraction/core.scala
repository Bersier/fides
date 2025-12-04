package fides.syntax.abstraction

import fides.syntax.types.*

// todo polymorphic abstractions?
//  Variance? Type bounds ?!

/**
  * Lambda abstraction literal
  */
final case class Lambda[I <: TopPoS, O <: TopPoS](body: OldCode[Bipo[I, O]]) extends OldCode[Cnst[?]]
// todo
