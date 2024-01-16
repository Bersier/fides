package fides.syntax.values

import fides.syntax.code.Val

/**
  * Added mainly for convenience, so annotations and error messages can be readily readable.
  */
final case class Str(value: String) extends Val[Str]
