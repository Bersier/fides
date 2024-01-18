package fides.syntax.code

import fides.syntax.identifiers.Identifier
import fides.syntax.meta.VarArgs

// todo add way to define custom Expr, Xctr, ...? As some light syntactic sugar?
//  Make the right abstraction accessible?
//  Static (higher-order) code functions

final case class ExprComponent[T <: ValType]
(name: ExprCompName, parameters: Code[VarArgs[Identifier]], expr: Code[Expr[T]]) extends Process

final case class Component(name: CompName, parameters: Code[VarArgs[Identifier]], body: Code[Process]) extends Process

// todo Beluga

type CompName = Nothing
type ExprCompName = Nothing
// todo refine VarArgs to something similar to HLists, but for dictionaries

// todo what about dictionary values? To represent dynamic abstractions
