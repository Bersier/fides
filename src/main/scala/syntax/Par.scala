package syntax

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * All the outgoing branches shall be executed.
  */
final class Par[ValueT <: Val](o: => Set[Taker[ValueT]]) extends Taker[ValueT] {
  val out = o

  override def apply(v: ValueT): Unit = for (o <- out) Future(o(v))
}
