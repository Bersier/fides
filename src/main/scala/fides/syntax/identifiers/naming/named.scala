package fides.syntax.identifiers.naming

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.immutable.ArraySeq
import scala.language.experimental.pureFunctions

trait Named:
  protected[identifiers] def name: String
object Named:
  private[identifiers] def from[N <: Named](constructor: String -> N)(using Context): N =
    from(constructor, newName())

  private[identifiers] def newName[N <: Named]() =
    symbols(currentSymbolIndex.getAndIncrement()).toString

  private[identifiers] def from[N <: Named](constructor: String -> N, name: String)(using Context): N =
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
end Named

val symbols: IndexedSeq[Char] = ArraySeq.from(
  Set.from('a' to 'z') ++
  Set.from('α' to 'ω') ++
  Set.from('א' to 'ת') ++
  Set.from('१' to '९') --
  Set('l', 'o', 'ν', 'ο', 'ח', 'י', 'ן', 'ז', 'ו')
).sorted
