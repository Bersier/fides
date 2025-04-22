package fides.syntax.code

import fides.syntax.identifiers.Identifier
import fides.syntax.meta.Args
import util.TList

// todo use lazy HOAS approach?

// todo polymorphic abstractions?
//  Variance? Type bounds ?!

final case class ExprComponent[T <: ValTop](
  name: Code[ExprCompName],
  parameters: Code[Args[Identifier]],
  expr: Code[Expr[T]],
) extends Process

final case class Component(
  name: Code[CompName],
  parameters: Code[Args[Identifier]],
  body: Code[Process],
) extends Process

final case class Application(name: Code[CompName])(arguments: Code[Mapping[?]]) extends Process

final case class Mapping[L <: TList[(Identifier, CodeType)]](arguments: L) extends CodeType
type CompName = Nothing
type ExprCompName = Nothing


// todo refine VarArgs to something similar to HLists, but for dictionaries
