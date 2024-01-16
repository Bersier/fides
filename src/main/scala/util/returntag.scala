package util

val Async = new Done
type Async = Async.Type

class Done:
  opaque type Type = Unit
  inline def apply(unit: Unit = ()): Type = ()

class Return[T]:
  opaque type Type = T
  inline def apply(inline value: T): Type = value
//  def apply2[Other <: Return[T]#Type](value: Other): Type = value.asInstanceOf[Type]
//  def apply3(using other: Return[T])(value: other.Type): Type = other.unapply(value)
  private inline def unapply(inline value: Type): T = value
  given Conversion[Type, T] with
    def apply(value: Type): T = unapply(value)
//object Return: // todo delete ('given' doesn't make sense)
//  given [T](using r: Return[T]): Conversion[r.Type, T] with
//    // for some reason, adding 'inline' at the beginning of the next line trips up the compiler...
//    def apply(value: r.Type): T = r.unapply(value)

def returnTagExample(): Unit =
  import scala.language.implicitConversions
  val fooReturn = new Done
  def foo: fooReturn.Type = if true then fooReturn() else foo
  println(foo)
  val barReturn = new Return[Int]
  def bar: barReturn.Type = if true then barReturn(0) else bar
  println(bar + 1)

object Loop:
  opaque type Return = Unit
  inline def done: Return = ()
