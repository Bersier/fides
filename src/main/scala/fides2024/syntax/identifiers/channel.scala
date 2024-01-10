package fides2024.syntax.identifiers

import fides2024.syntax.{Val, ValType}

/**
  * A type of location used for channels
  *
  * @tparam T the type of the values that transit through the channel
  */
open class Channel[T <: ValType] protected(name: String) extends Identifier(name), Val[Channel[T]]:
  override def toString: String = s"@$name"
object Channel extends LocationBuilder[Channel]:
  protected def constructor[T <: ValType](name: String): Channel[T] = new Channel(name)
end Channel
