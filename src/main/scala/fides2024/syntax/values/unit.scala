package fides2024.syntax.values

import fides2024.syntax.kinds.Val

/**
  * A value that doesn't carry any information beyond causality
  * (since the sending of any value occurs before its reception).
  *
  * The corresponding type, U.type, is like the Unit type in Fides.
  */
case object U extends Val[U.type]
