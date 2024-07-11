package util

import scala.annotation.targetName
import scala.annotation.unchecked.uncheckedVariance
import scala.compiletime.constValue
import scala.compiletime.ops.int.{+, <=}
import scala.util.Random

// todo move to Misc repository

trait Bag[+T, +Size <: Int] extends Iterable[T], FiniteObservable[T]:
  @targetName("plus") def +[U](element: U): Bag[T | U, (Size + 1) @uncheckedVariance]

object Bag:
  def apply(): Bag[Nothing, 0] = Zero
  def apply[T](element: T): NonEmpty[T, 1] = One(element)
  def apply[T](e1: T, e2: T): NonEmpty[T, 2] = Two(e1, e2)
  def apply[T](e1: T, e2: T, e3: T): NonEmpty[T, 3] = Three(e1, e2, e3)
  def apply[T](e1: T, e2: T, e3: T, e4: T): NonEmpty[T, 4] = Four(e1, e2, e3, e4)

  trait NonEmpty[+T, Size <: Int] extends Bag[T, Size]:
    def randomSamplePoint(generator: Random = Random): T

    final def randomSample[S <: Int](size: S, generator: Random = Random)(using 0 <= S =:= true): Bag[T, S] =
      randomSampleUnsafe(size, generator).asInstanceOf[Bag[T, S]]

    final def randomSampleWithoutRepetitions[S <: Int]
    (size: S, generator: Random = Random)(using 0 <= S =:= true, S <= Size =:= true): Bag[T, S] =
      randomSampleWithoutRepetitionsUnsafe(size, generator).asInstanceOf[Bag[T, S]]

    protected def randomSampleUnsafe(size: Int, generator: Random): Bag[T, Int] =
      if size == 0 then Zero else randomSampleUnsafe(size - 1, generator) + randomSamplePoint(generator)

    protected[Bag] def randomSampleWithoutRepetitionsUnsafe(size: Int, generator: Random): Bag[T, Int]

  end NonEmpty

  private object Zero extends Bag[Nothing, 0]:
    def iterator: Iterator[Nothing] = Iterator.empty

    def foreach(observer: DefiniteObserver[Nothing]): Async = observer.withSize(0)

    @targetName("plus") def +[U](element: U): Bag[U, 1] = One(element)

    override def size: 0 = 0

  end Zero

  private class One[+T](e: T) extends NonEmpty[T, 1]:
    def iterator: Iterator[T] = Iterator(e)

    def foreach(observer: DefiniteObserver[T]): Async = Async:
      val _ = observer.withSize(1)
      val _ = observer.update(e)

    @targetName("plus") def +[U](element: U): Bag[T | U, 2] = Two(e, element)

    override def size: 1 = 1

    def randomSamplePoint(generator: Random): T = e

    def randomSampleWithoutRepetitionsUnsafe(size: Int, generator: Random): Bag[T, Int] =
      if size == 0 then Zero else this

  end One

  private class Two[+T](e1: T, e2: T) extends NonEmpty[T, 2]:
    def iterator: Iterator[T] = Iterator(e1, e2)

    def foreach(observer: DefiniteObserver[T]): Async = Async:
      val _ = observer.withSize(2)
      val _ = observer.update(e1)
      val _ = observer.update(e2)

    @targetName("plus") def +[U](element: U): Bag[T | U, 3] = Three(e1, e2, element)

    override def size: 2 = 2

    def randomSamplePoint(generator: Random): T =
      if generator.nextBoolean() then e1 else e2

    protected[Bag] def randomSampleWithoutRepetitionsUnsafe(size: Int, generator: Random): Bag[T, Int] = size match
      case 0 => Zero
      case 1 => One(randomSamplePoint(generator))
      case 2 => this
      case i => throw AssertionError(i)

  end Two

  private class Three[+T](e1: T, e2: T, e3: T) extends NonEmpty[T, 3]:
    def iterator: Iterator[T] = Iterator(e1, e2, e3)

    def foreach(observer: DefiniteObserver[T]): Async = Async:
      val _ = observer.withSize(3)
      val _ = observer.update(e1)
      val _ = observer.update(e2)
      val _ = observer.update(e3)

    @targetName("plus") def +[U](element: U): Bag[T | U, 4] = Four(e1, e2, e3, element)

    override def size: 3 = 3

    def randomSamplePoint(generator: Random): T = (generator.nextBoolean(), generator.nextBoolean()) match
      case (false, false) => e1
      case (false, true ) => e2
      case (true , false) => e3
      case (true , true ) => randomSamplePoint(generator)

    protected[Bag] def randomSampleWithoutRepetitionsUnsafe(size: Int, generator: Random): Bag[T, Int] = size match
      case 0 => Zero
      case 1 => One(randomSamplePoint(generator))
      case 2 => randomSampleWithoutRepetitionsOfSize2(generator)
      case 3 => this
      case i => throw AssertionError(i)

    private def randomSampleWithoutRepetitionsOfSize2(generator: Random): Two[T] =
      (generator.nextBoolean(), generator.nextBoolean()) match
        case (false, false) => Two(e2, e3)
        case (false, true ) => Two(e1, e3)
        case (true , false) => Two(e1, e2)
        case (true , true ) => randomSampleWithoutRepetitionsOfSize2(generator)

  end Three

  private class Four[+T](e1: T, e2: T, e3: T, e4: T) extends NonEmpty[T, 4]:
    def iterator: Iterator[T] = Iterator(e1, e2, e3, e4)

    def foreach(observer: DefiniteObserver[T]): Async = Async:
      val _ = observer.withSize(4)
      val _ = observer.update(e1)
      val _ = observer.update(e2)
      val _ = observer.update(e3)
      val _ = observer.update(e4)

    @targetName("plus") def +[U](element: U): Bag[T | U, 5] = Any(List(element, e4, e3, e2, e1), 5)

    override def size: 4 = 4

    def randomSamplePoint(generator: Random): T = (generator.nextBoolean(), generator.nextBoolean()) match
      case (false, false) => e1
      case (false, true ) => e2
      case (true , false) => e3
      case (true , true ) => e4

    protected[Bag] def randomSampleWithoutRepetitionsUnsafe(size: Int, generator: Random): Bag[T, Int] = size match
      case 0 => Zero
      case 1 => One(randomSamplePoint(generator))
      case 2 => randomSampleWithoutRepetitionsOfSize3(generator).randomSampleWithoutRepetitionsUnsafe(2, generator)
      case 3 => randomSampleWithoutRepetitionsOfSize3(generator)
      case 4 => this
      case i => throw AssertionError(i)

    private def randomSampleWithoutRepetitionsOfSize3(generator: Random): Three[T] =
      (generator.nextBoolean(), generator.nextBoolean()) match
        case (false, false) => Three(e2, e3, e4)
        case (false, true ) => Three(e1, e3, e4)
        case (true , false) => Three(e1, e2, e4)
        case (true , true ) => Three(e1, e2, e3)

  end Four

  private class Any[+T, Size <: Int](elements: List[T], override val size: Size) extends NonEmpty[T, Size]:
    def iterator: Iterator[T] = elements.iterator

    def foreach(observer: DefiniteObserver[T]): Async = Async:
      val _ = observer.withSize(elements.length)
      for element <- elements do
        observer.update(element)

    @targetName("plus") def +[U](element: U): Bag[T | U, Size + 1] =
      Any(element :: elements, (size + 1).asInstanceOf[Size + 1])

    def randomSamplePoint(generator: Random): T =
      def randomSamplePoint(elements: List[T], length: Int): T =
        if generator.nextDouble() < 1.0 / length then elements.head else
          randomSamplePoint(elements.tail, length - 1)
      randomSamplePoint(elements, size)

    protected override def randomSampleUnsafe(size: Int, generator: Random): Bag[T, Int] =
      inline def pickProbability(inline sampleSize: Int, inline size: Int): Double =
        1 - math.pow(1 - 1.0 / size, sampleSize)
      def randomSample(sampleSize: Int, size: Int, elements: List[T], acc: List[T]): List[T] =
        if sampleSize == 0 then acc else
          if generator.nextDouble() < pickProbability(sampleSize, size)
          then randomSample(sampleSize - 1, size, elements, elements.head :: acc)
          else randomSample(sampleSize, size - 1, elements.tail, acc)
      Any(randomSample(size, this.size, elements, Nil), size)

    protected[Bag] def randomSampleWithoutRepetitionsUnsafe(size: Int, generator: Random): Bag[T, Int] =
      inline def pickProbability(inline sampleSize: Int, inline size: Int): Double =
        sampleSize.toDouble / size.toDouble
      def randomSample(sampleSize: Int, size: Int, elements: List[T], acc: List[T]): List[T] =
        if sampleSize == 0 then acc else
          if generator.nextDouble() < pickProbability(sampleSize, size)
          then randomSample(sampleSize - 1, size -1, elements.tail, elements.head :: acc)
          else randomSample(sampleSize, size - 1, elements.tail, acc)
      Any(randomSample(size, this.size, elements, Nil), size)

  end Any

end Bag
