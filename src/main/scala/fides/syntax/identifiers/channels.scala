package fides.syntax.identifiers

import fides.syntax.code.{Lit, OffBot, OffTop, Polar, Ntrl, ValBot, ValTop}
import izumi.reflect.Tag

/**
  * Data type for Channels
  */
sealed trait ChanT[+InpT >: ValBot, -OutT <: ValTop] extends IDT

/**
  * Channel identifier
  *
  * At runtime, a channel identifier value does not keep track of its type.
  *
  * @tparam InpT this identifier, when used to receive, might receive any value of this type
  * @tparam OutT this identifier, when used to send, can be used to send any value of this type
  */
final class Chan[InpT >: ValBot, OutT <: ValTop] private[identifiers] extends Lit, Ntrl[ChanT[InpT, OutT]]

type InpChan[+T <: ValTop] = Chan[T, OffBot]
object InpChan:
  def apply[T <: ValTop](): InpChan[T] = Chan()
end InpChan

type OutChan[-T <: ValTop] = Chan[OffTop, T]
object OutChan:
  def apply[T <: ValTop](): OutChan[T] = Chan()
end OutChan

type Channel[T <: ValTop] = Chan[T, T]
object Channel:
  def apply[T <: ValTop](): Channel[T] = Chan()
end Channel
