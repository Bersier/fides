package fides.syntax.identifiers

import fides.syntax.core.Code
import fides.syntax.types.{ChannelID, ChannelT, Ntrl, OffBotT, OffTopT, TopT}

/**
  * Channel identifier
  *
  * At runtime, a channel identifier value does not keep track of its type.
  *
  * @tparam InpT this identifier, when used to receive, might receive any value of this type
  * @tparam OutT this identifier, when used to send, can be used to send any value of this type
  */
final case class Chan[K <: ChannelID, InpT >: OutT, OutT <: TopT] private[identifiers](
  private val k: K,
) extends Code[Ntrl[ChannelT[K, InpT, OutT]]]

object InpChan:
  def apply[T <: TopT](): Chan[?, T, OffBotT] = Chan(ChannelID())
  def apply[K <: ChannelID & Singleton, T <: TopT](k: K): Chan[K, T, OffBotT] = Chan(k)
end InpChan

object OutChan:
  def apply[T <: TopT](): Chan[?, OffTopT, T] = Chan(ChannelID())
  def apply[K <: ChannelID & Singleton, T <: TopT](k: K): Chan[K, OffTopT, T] = Chan(k)
end OutChan

object Channel:
  def apply[T <: TopT](): Chan[?, T, T] = Chan(ChannelID())
  def apply[K <: ChannelID & Singleton, T <: TopT](k: K): Chan[K, T, T] = Chan(k)
end Channel
