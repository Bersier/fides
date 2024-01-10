package fides2024.syntax.identifiers

import fides2024.syntax.kinds.{Code, Val}

private[identifiers] def internalIDString(iD: Code[Val[Identifier]]): String = iD match
  case c: Identifier => c.name
  case _             => iD.toString
