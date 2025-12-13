package fides.syntax.machinery

import fides.syntax.values.*

/**
  * Parent type of all actual Fides code
  * <br><br>
  * This additional type wrapper is important mainly because it keeps track of [[M]] invariantly.
  * It also provides the required flexibility for escape matchers to have an [[M]]
  * type that is a supertype of that of actual escapes.
  */
trait Code[M <: TopM] private[syntax]()
object Code:
  given [
    D1 <: TopD, D2 <: TopD, P1 <: TopP, P2 <: TopP,
    G1 <: PolarG[D1, P1], G2 <: PolarG[D2, P2],
    M1 <: ConsHM[G1], M2 <: ConsHM[G2],
  ] => (c1: Code[M1], c2: Code[M2]) => Code[PairM[D1, D2, P1, P2, G1, G2, M1, M2]] = Pair(c1, c2)
end Code

trait Code2[M <: TopM2] private[syntax]() // todo add `TM+` and `TM-`. Alternatively, add a quote name list for context
// todo perhaps reduction could even be applied just-in-time, so not in advance as soon as a quote is encountered,
//  but lazily inside as we go... does that make any sense?

@deprecated
trait OldCode[+G <: TopG] private[syntax]()
