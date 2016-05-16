
package object syntax {
  abstract class In[InT <: Val] protected[this] extends Taker[InT] {
    protected var continuation: InT => Unit

    def apply(in: InT): Unit = synchronized(continuation(in))
  }
}
