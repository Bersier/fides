package fides.syntax2025.machinery

import fides.syntax2025.values.*

/**
  * Parent type of all actual Fides code
  * <br><br>
  * This additional type wrapper is important mainly because it keeps track of [[M]] invariantly.
  * It also provides the required flexibility for escape matchers to have an [[M]]
  * type that is a supertype of that of actual escapes.
  */
trait Code[M <: TopM] private[syntax2025]()
object Code:
  given [
    D1 <: TopD, D2 <: TopD, P1 <: TopP, P2 <: TopP,
    G1 <: PolarG[D1, P1], G2 <: PolarG[D2, P2],
    M1 <: GenHM[G1], M2 <: GenHM[G2],
  ] => (c1: Code[M1], c2: Code[M2]) => Code[PairM[D1, D2, P1, P2, G1, G2, M1, M2]] = Pair(c1, c2)
end Code

@deprecated
trait OldCode[+G <: TopG] private[syntax2025]()
