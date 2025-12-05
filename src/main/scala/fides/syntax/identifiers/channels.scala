package fides.syntax.identifiers

import fides.syntax.types.*

/**
  * Channel identifier
  *
  * At runtime, a channel identifier value does not keep track of its type.
  *
  * @tparam InpD this identifier, when used to receive, might receive any value of this type
  * @tparam OutD this identifier, when used to send, can be used to send any value of this type
  */
final case class Chan[K <: ID, InpD >: OutD, OutD <: TopD] private[identifiers](
  private val k: K,
) extends OldCode[Ntrl[ChannelD[K, InpD, OutD]]]

object InpChan:
  def apply[D <: TopD](): Chan[?, D, OffBotD] = Chan(ID())
  def apply[K <: ID & Singleton, D <: TopD](k: K): Chan[K, D, OffBotD] = Chan(k)
end InpChan

object OutChan:
  def apply[D <: TopD](): Chan[?, OffTopD, D] = Chan(ID())
  def apply[K <: ID & Singleton, D <: TopD](k: K): Chan[K, OffTopD, D] = Chan(k)
end OutChan

object Channel:
  def apply[D <: TopD](): Chan[?, D, D] = Chan(ID())
  def apply[K <: ID & Singleton, D <: TopD](k: K): Chan[K, D, D] = Chan(k)
end Channel
