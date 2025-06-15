package fides.syntax.identifiers

import fides.syntax.core.Code
import fides.syntax.types.{ChanT, Ntrl, OffBotT, OffTopT, TopT}

/**
  * Channel identifier
  *
  * At runtime, a channel identifier value does not keep track of its type.
  *
  * @tparam InpT this identifier, when used to receive, might receive any value of this type
  * @tparam OutT this identifier, when used to send, can be used to send any value of this type
  */
final class Chan[InpT >: OutT, OutT <: TopT] extends Code[Ntrl[ChanT[InpT, OutT]]]

object InpChan:
  def apply[T <: TopT](): Chan[T, OffBotT] = Chan()
end InpChan

object OutChan:
  def apply[T <: TopT](): Chan[OffTopT, T] = Chan()
end OutChan

object Channel:
  def apply[T <: TopT](): Chan[T, T] = Chan()
end Channel
