package util

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.immutable.ArraySeq

def newSymbol() = symbols(currentSymbolIndex.getAndIncrement()).toString

private val currentSymbolIndex = AtomicInteger(0)

// This, together with -Ysafe-init-global, causes impractically slow compilation.
val symbols: IndexedSeq[Char] = ArraySeq.from(
  Set.from('a' to 'z') ++
  Set.from('α' to 'ω') ++
  Set.from('א' to 'ת') ++
  Set.from('१' to '९') --
  Set('l', 'o', 'ν', 'ο', 'ח', 'י', 'ן', 'ז', 'ו')
).sorted
