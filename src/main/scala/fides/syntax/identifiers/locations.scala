package fides.syntax.identifiers

import fides.syntax.code.{Code, CodeType}

import java.util.concurrent.atomic.AtomicInteger
import scala.language.experimental.pureFunctions

trait Location protected(val name: String) extends CodeType, Code[Location]
object Location:
  private[identifiers] def from[I <: Location](constructor: String -> I)(using Context): I =
    from(constructor, newName())

  private[identifiers] def newName[I <: Location]() =
    util.symbols(currentSymbolIndex.getAndIncrement()).toString

  private[identifiers] def from[I <: Location](constructor: String -> I, name: String)(using Context): I =
    val location = constructor(qualifiedName(name))
    val previous   = summon[Context].names.putIfAbsent(name, location)
    require(
      requirement = previous.isEmpty,
      message = s"$name is already used for ${previous.get.toString}_${previous.get.hashCode().toHexString}",
    )
    location

  private def qualifiedName(name: String)(using Context): String =
    val globalPrefix = Some(summon[Context].prefix).filter(_.nonEmpty).map(_ + ".").getOrElse("")
    s"$globalPrefix$name"

  private val currentSymbolIndex = AtomicInteger(0)
end Location
