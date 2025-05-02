package fides.syntax.abstraction

import fides.syntax.core.Code
import fides.syntax.identifiers.Identifier
import fides.syntax.meta.Args
import fides.syntax.types.{Args, CodeType, Expr, Process, ValTop}
import util.TList

// todo use lazy HOAS approach?

// todo polymorphic abstractions?
//  Variance? Type bounds ?!

final case class Lambda()

//final case class Mapping[L <: TList[(Identifier, CodeType)]](arguments: L) extends CodeType

// todo refine VarArgs to something similar to HLists, but for dictionaries
