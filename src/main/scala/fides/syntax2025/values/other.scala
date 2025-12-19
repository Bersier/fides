package fides.syntax2025.values

import fides.syntax2025.machinery.*

/**
  * Added mainly for convenience, so annotations and error messages can be readily readable.
  *
  * Unmanipulable on purpose, because it's not really part of the language.
  */
final case class Str(value: String) extends Code[GenM[NtrlG[StrD]]]
