package fides.syntax.identifiers

import fides.syntax.core.Code
import fides.syntax.types.{ChannelT, Ntrl, OffBotT, OffTopT, TopT}
import util.ID

/**
  * Channel identifier
  *
  * At runtime, a channel identifier value does not keep track of its type.
  *
  * @tparam InpT this identifier, when used to receive, might receive any value of this type
  * @tparam OutT this identifier, when used to send, can be used to send any value of this type
  */
final case class Chan[K <: ID, InpT >: OutT, OutT <: TopT](private val k: K) extends Code[Ntrl[ChannelT[K, InpT, OutT]]]

object InpChan:
  def apply[K <: ID, T <: TopT](k: K): Chan[K, T, OffBotT] = Chan(k)
end InpChan

object OutChan:
  def apply[K <: ID, T <: TopT](k: K): Chan[K, OffTopT, T] = Chan(k)
end OutChan

object Channel:
  def apply[K <: ID, T <: TopT](k: K): Chan[K, T, T] = Chan(k)
end Channel
