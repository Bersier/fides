package fides.syntax.identifiers

import fides.syntax.code.{Code, Val}

private[identifiers] def internalIDString(iD: Code[Val[Identifier]]): String = iD match
  case c: Identifier => c.name
  case _             => iD.toString
