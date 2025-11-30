package fides.syntax.abstraction

import fides.syntax.types.{Bipo, Cnst, Code, TopPoS}

// todo polymorphic abstractions?
//  Variance? Type bounds ?!

/**
  * Lambda abstraction literal
  */
final case class Lambda[I <: TopPoS, O <: TopPoS](body: Code[Bipo[I, O]]) extends Code[Cnst[?]]
// todo
