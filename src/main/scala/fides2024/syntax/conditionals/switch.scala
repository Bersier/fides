package fides2024.syntax.conditionals

import fides2024.syntax.identifiers.Identifier
import fides2024.syntax.kinds.{Code, CodeType, Component, Ptrn, Val, VarArgs}

final case class Switch(cases : Code[VarArgs[Case]]) extends Ptrn[Identifier, Identifier]

final case class Case(testValue: Code[Val[Identifier]], body: Code[Component]) extends Code[Case], CodeType
