package util

sealed trait Async
object Async {
  private[this] val singleton: Async = new {}
  inline def apply(computation: Unit = ()): Async = singleton
}

trait Observer[-T] extends (T => Async) {
  def update(t: T): Async = apply(t)
}

trait DefiniteObserver[-T] extends (T => Async) {
  def withSize(size: Int): Async
}

final class TrivialDefiniteObserver[-T](observer: Observer[T]) extends DefiniteObserver[T] {
  def withSize(size: Int): Async = Async()
  export observer.*
}

trait Observable[+T] extends (Observer[T] => Async) {
  def apply(observer: Observer[T]): Async = foreach(observer)
  def foreach(observer: Observer[T]): Async
}

trait FiniteObservable[+T] extends Observable[T] with (DefiniteObserver[T] => Async) {
  def apply(observer: DefiniteObserver[T]): Async = foreach(observer)
  def foreach(observer: DefiniteObserver[T]): Async
  def foreach(observer: Observer[T]): Async = foreach(TrivialDefiniteObserver(observer))
}
// Reduce?

def foo(): Unit = {
  val a: FiniteObservable[Int] = ???
  for i <- a do Async {
    println(i)
  }
}
