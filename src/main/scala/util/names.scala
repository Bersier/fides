package util

import scala.collection.immutable.ArraySeq

val symbols: IndexedSeq[Char] = ArraySeq.from(
  Set.from('a' to 'z') ++
  Set.from('α' to 'ω') ++
  Set.from('א' to 'ת') ++
  Set.from('१' to '९') --
  Set('l', 'o', 'ν', 'ο')
).sorted
