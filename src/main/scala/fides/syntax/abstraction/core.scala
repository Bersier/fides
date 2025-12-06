package fides.syntax.abstraction

import fides.syntax.machinery.*

// todo polymorphic abstractions?
//  Variance? Type bounds ?!

/**
  * Lambda abstraction literal
  */
final case class Lambda[I <: TopPoG, O <: TopPoG](body: OldCode[BipoG[I, O]]) extends OldCode[OldCnstG[?]]
// todo
