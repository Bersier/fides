package doodle

object RuntimeType extends App {
  class A
  object B extends A
  object C extends A

  val b: A = B
  val c: A = C
  val f: Nothing => Unit = (b: B.type) => {}

  f.asInstanceOf[B.type => Unit](b.asInstanceOf[B.type])

  def applyF(a: A): Unit = {
    f.asInstanceOf[A => Unit](a) // "thanks to" erasure, this works, even though f is not of type
  }                              // A => Unit

  applyF(b)
  applyF(c)                      // fails at runtime, as it should
}
