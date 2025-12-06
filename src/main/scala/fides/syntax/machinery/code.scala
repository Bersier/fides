package fides.syntax.machinery

/**
  * Parent type of all actual Fides code
  * <br><br>
  * This additional type wrapper is important mainly because it keeps track of [[M]] invariantly.
  * It also provides the required flexibility for escape matchers to have an [[M]]
  * type that is a supertype of that of actual escapes.
  */
trait Code[M <: TopM] private[syntax]()

@deprecated
trait OldCode[+G <: TopG] private[syntax]()
