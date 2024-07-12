package util

import scala.annotation.targetName

/**
  * Observer base trait, with methods shared by all observers
  *
  * Each observer only observes one observable.
  */
private transparent trait Observer[-T] extends (T => Async):
  /**
    * Passes a value to this observer.
    */
  inline def update(inline t: T): Async = apply(t)
end Observer

/**
  * Most primitive type of observer. Observes values,
  * whithout ever knowing whether all values have been observed.
  */
trait IndefiniteObserver[-T] extends Observer[T]

/**
  * Observer for finite observables whose size is eventually known.
  */
trait DefiniteObserver[-T] extends Observer[T]:
  /**
    * Lets this observer know the size of the observed observable.
    *
    * This method should always be called by the obervable eventually (exactly once).
    *
    * @param size of the observable
    */
  def withSize(size: Int): Async
end DefiniteObserver

/**
  * An indefinite observer can trivially be converted to a definite observer,
  * by ignoring the [[withSize]] call (i.e. have it do nothing).
  */
final class TrivialDefiniteObserver[-T](observer: IndefiniteObserver[T]) extends DefiniteObserver[T]:
  def withSize(size: Int): Async = Async()
  export observer.*
end TrivialDefiniteObserver

/**
  * Most primitive type of observable.
  */
trait Observable[+T] extends (IndefiniteObserver[T] => Async):
  /**
    * Applies the given observer to this observable.
    */
  inline def apply(observer: IndefiniteObserver[T]): Async = foreach(observer)
  /**
    * Applies the given observer to this observable.
    */
  def foreach(observer: IndefiniteObserver[T]): Async

end Observable

// todo monoid
extension [S](o1: Observable[S])
  @targetName("plus") def +[T](o2: Observable[T]): Observable[S | T] =
    (observer: IndefiniteObserver[S | T]) => Async:
      val _ = o1.foreach(observer)
      val _ = o2.foreach(observer)

extension [S](o1: FiniteObservable[S])
  @targetName("plus") def +[T](o2: FiniteObservable[T]): FiniteObservable[S | T] =
    (observer: DefiniteObserver[S | T]) => Async:
      val _ = o1.foreach(observer)
      val _ = o2.foreach(observer)

/**
  * Finite observable, whose size is eventually known
  */
trait FiniteObservable[+T] extends Observable[T] with (DefiniteObserver[T] => Async):
  /**
    * Applies the given observer to this observable.
    */
  inline def apply(observer: IndefiniteObserver[T] | DefiniteObserver[T]): Async = observer match
    case i: IndefiniteObserver[T @unchecked] => foreach(i)
    case d:   DefiniteObserver[T @unchecked] => foreach(d)

  /**
    * Applies the given observer to this observable.
    */
  inline def apply(observer: DefiniteObserver[T]): Async = foreach(observer)

  def foreach(observer: IndefiniteObserver[T]): Async = foreach(TrivialDefiniteObserver(observer))

  /**
    * Applies the given observer to this observable.
    */
  def foreach(observer: DefiniteObserver[T]): Async

end FiniteObservable

extension [T](iterable: Iterable[T])
  /**
    * @return an observable corresponding to this iterator
    */
  def asObservable: Observable[T] = (observer: IndefiniteObserver[T]) =>
    Async(iterable.foreach(value => observer.update(value)))

  /**
    * @return a finite observable corresponding to this finite iterator
    */
  def asFiniteObservable: FiniteObservable[T] = (observer: DefiniteObserver[T]) => Async:
    var counter = 0
    for value <- iterable do
      val _ = observer.update(value)
      counter += 1
    observer.withSize(counter)

private def example(observable: FiniteObservable[Int]): Unit =
  for i <- observable do Async:
    println(i)
