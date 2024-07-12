package util

import scala.annotation.targetName
import scala.compiletime.{constValue, error}
import scala.compiletime.ops.int.{+, <=}
import scala.util.Random

// todo move to Misc repository

@main def testBag(): Unit =
  val bag: Bag[Int, 5] = Bag(1, 2, 3, 4, 5)
  println(bag)
  println()
  for i <- 0 until 10 do
    println(bag.randomSample(20))

trait Bag[+T, Size <: Int] extends Bag.Unsized[T]:
  final def randomSamplePoint(generator: Random = Random)(using 1 <= Size =:= true): T =
    randomSamplePointUnsafe(generator)

  final def randomSample[S <: Int & Singleton](size: S, generator: Random = Random)
  (using 0 <= S =:= true): Bag[T, S] =
    randomSampleUnsafe(size, generator).asInstanceOf[Bag[T, S]]

  final def randomSampleWithoutRepetitions[S <: Int & Singleton](size: S, generator: Random = Random)
  (using 0 <= S =:= true, S <= Size =:= true): Bag[T, S] =
    randomSampleWithoutRepetitionsUnsafe(size, generator).asInstanceOf[Bag[T, S]]

  protected def randomSamplePointUnsafe(generator: Random): T

  // todo confusing override logic
  protected def randomSampleUnsafe(size: Int, generator: Random): Bag.Unsized[T] = size match
    case 0 => Bag.Zero
    case 1 => Bag.One(randomSamplePointUnsafe(generator))
    case _ => Bag.Any(iterator.toList.reverse, this.size).randomSampleUnsafe(size, generator)

  protected def randomSampleWithoutRepetitionsUnsafe(size: Int, generator: Random): Bag.Unsized[T]

  final override def toString: String = s"Bag(${iterator.mkString(", ")})"

object Bag:
  trait Unsized[+T] extends Iterable[T], FiniteObservable[T]:
    @targetName("plus") def +[U](element: U): Unsized[T | U]
  end Unsized

  def apply(): Bag[Nothing, 0] = Zero
  def apply[T](element: T): Bag[T, 1] = One(element)
  def apply[T](e1: T, e2: T): Bag[T, 2] = Two(e1, e2)
  def apply[T](e1: T, e2: T, e3: T): Bag[T, 3] = Three(e1, e2, e3)
  def apply[T](e1: T, e2: T, e3: T, e4: T): Bag[T, 4] = Four(e1, e2, e3, e4)
  inline def apply[T, S <: Int & Singleton](e1: T, e2: T, e3: T, e4: T, e5: T, elements: T*): Bag[T, S] =
    assert(constValue[S] == elements.length + 5)
    applyUnsafe(IndexedSeq(e1, e2, e3, e4, e5) ++ elements).asInstanceOf[Bag[T, S]]

  private def applyUnsafe[T](elements: IndexedSeq[T]): Bag[T, Int] =
    Any(elements.toList.reverse, elements.length)

  private object Zero extends Bag[Nothing, 0]:
    def iterator: Iterator[Nothing] = Iterator.empty

    def foreach(observer: DefiniteObserver[Nothing]): Async = observer.withSize(0)

    @targetName("plus") def +[U](element: U): Bag[U, 1] = One(element)

    override def size: 0 = 0

    protected def randomSamplePointUnsafe(generator: Random): Nothing =
      throw new AssertionError("Unreachable")

    protected def randomSampleWithoutRepetitionsUnsafe(size: Int, generator: Random): Unsized[Nothing] =
      size match
        case 0 => this
        case _ => throw new AssertionError(size)
  end Zero

  private class One[+T](e: T) extends Bag[T, 1]:
    def iterator: Iterator[T] = Iterator(e)

    def foreach(observer: DefiniteObserver[T]): Async = Async:
      val _ = observer.withSize(1)
      val _ = observer.update(e)

    @targetName("plus") def +[U](element: U): Bag[T | U, 2] = Two(e, element)

    override def size: 1 = 1

    def randomSamplePointUnsafe(generator: Random): T = e

    def randomSampleWithoutRepetitionsUnsafe(size: Int, generator: Random): Unsized[T] =
      if size == 0 then Zero else this

  end One

  private class Two[+T](e1: T, e2: T) extends Bag[T, 2]:
    def iterator: Iterator[T] = Iterator(e1, e2)

    def foreach(observer: DefiniteObserver[T]): Async = Async:
      val _ = observer.withSize(2)
      val _ = observer.update(e1)
      val _ = observer.update(e2)

    @targetName("plus") def +[U](element: U): Bag[T | U, 3] = Three(e1, e2, element)

    override def size: 2 = 2

    def randomSamplePointUnsafe(generator: Random): T =
      if generator.nextBoolean() then e1 else e2

    protected def randomSampleWithoutRepetitionsUnsafe(size: Int, generator: Random): Unsized[T] =
      size match
        case 0 => Zero
        case 1 => One(randomSamplePoint(generator))
        case 2 => this
        case _ => throw AssertionError(size)

  end Two

  private class Three[+T](e1: T, e2: T, e3: T) extends Bag[T, 3]:
    def iterator: Iterator[T] = Iterator(e1, e2, e3)

    def foreach(observer: DefiniteObserver[T]): Async = Async:
      val _ = observer.withSize(3)
      val _ = observer.update(e1)
      val _ = observer.update(e2)
      val _ = observer.update(e3)

    @targetName("plus") def +[U](element: U): Bag[T | U, 4] = Four(e1, e2, e3, element)

    override def size: 3 = 3

    def randomSamplePointUnsafe(generator: Random): T =
      (generator.nextBoolean(), generator.nextBoolean()) match
        case (false, false) => e1
        case (false, true ) => e2
        case (true , false) => e3
        case (true , true ) => randomSamplePointUnsafe(generator)

    protected[Bag] def randomSampleWithoutRepetitionsUnsafe(size: Int, generator: Random): Unsized[T] =
      size match
        case 0 => Zero
        case 1 => One(randomSamplePointUnsafe(generator))
        case 2 => randomSampleWithoutRepetitionsOfSize2(generator)
        case 3 => this
        case _ => throw AssertionError(size)

    private def randomSampleWithoutRepetitionsOfSize2(generator: Random): Two[T] =
      (generator.nextBoolean(), generator.nextBoolean()) match
        case (false, false) => Two(e2, e3)
        case (false, true ) => Two(e1, e3)
        case (true , false) => Two(e1, e2)
        case (true , true ) => randomSampleWithoutRepetitionsOfSize2(generator)

  end Three

  private class Four[+T](e1: T, e2: T, e3: T, e4: T) extends Bag[T, 4]:
    def iterator: Iterator[T] = Iterator(e1, e2, e3, e4)

    def foreach(observer: DefiniteObserver[T]): Async = Async:
      val _ = observer.withSize(4)
      val _ = observer.update(e1)
      val _ = observer.update(e2)
      val _ = observer.update(e3)
      val _ = observer.update(e4)

    @targetName("plus") def +[U](element: U): Bag[T | U, 5] = Any(List(element, e4, e3, e2, e1), 5)

    override def size: 4 = 4

    def randomSamplePointUnsafe(generator: Random): T =
      (generator.nextBoolean(), generator.nextBoolean()) match
        case (false, false) => e1
        case (false, true ) => e2
        case (true , false) => e3
        case (true , true ) => e4

    protected def randomSampleWithoutRepetitionsUnsafe(size: Int, generator: Random): Unsized[T] =
      size match
        case 0 => Zero
        case 1 => One(randomSamplePointUnsafe(generator))
        case 2 => randomSampleWithoutRepetitionsOfSize3(generator).randomSampleWithoutRepetitionsUnsafe(2, generator)
        case 3 => randomSampleWithoutRepetitionsOfSize3(generator)
        case 4 => this
        case _ => throw AssertionError(size)

    private def randomSampleWithoutRepetitionsOfSize3(generator: Random): Three[T] =
      (generator.nextBoolean(), generator.nextBoolean()) match
        case (false, false) => Three(e2, e3, e4)
        case (false, true ) => Three(e1, e3, e4)
        case (true , false) => Three(e1, e2, e4)
        case (true , true ) => Three(e1, e2, e3)

  end Four

  private class Any[+T, Size <: Int](elements: List[T], override val size: Size) extends Bag[T, Size]:
    def iterator: Iterator[T] = elements.reverseIterator

    def foreach(observer: DefiniteObserver[T]): Async = Async:
      val _ = observer.withSize(elements.length)
      for element <- elements do
        observer.update(element)

    @targetName("plus") def +[U](element: U): Bag[T | U, Size + 1] =
      Any(element :: elements, (size + 1).asInstanceOf[Size + 1])

    def randomSamplePointUnsafe(generator: Random): T =
      def randomSamplePoint(elements: List[T], length: Int): T =
        if bernoulliSample(1.0 / length, generator) then elements.head else
          randomSamplePoint(elements.tail, length - 1)
      randomSamplePoint(elements, size)

    protected[Bag] override def randomSampleUnsafe(size: Int, generator: Random): Unsized[T] =
      // todo dubious numeric stability
      inline def repetitionCount(inline sampleSize: Int, inline size: Int): Int =
        val q = 1 - 1.0 / size
        val r = q * size
        def repetitionCount(randomDouble: Double, probability: Double, count: Int): Int =
          assert(count <= sampleSize)
          if randomDouble < probability then count else
            val newProbability = (sampleSize - count) / (count + 1.0) * probability / r
            repetitionCount(randomDouble - probability, newProbability, count + 1)
        if size == 1 then sampleSize else
          repetitionCount(generator.nextDouble(), math.pow(q, sampleSize), 0)
      def randomSample(sampleSize: Int, size: Int, elements: List[T], acc: List[T]): List[T] =
        if sampleSize == 0 then acc else
          val count = repetitionCount(sampleSize, size)
          randomSample(sampleSize - count, size - 1, elements.tail, withRepeated(elements.head, count, acc))
      optimizedUnsafe(randomSample(size, this.size, elements, Nil), size)

    protected def randomSampleWithoutRepetitionsUnsafe(size: Int, generator: Random): Unsized[T] =
      inline def pickProbability(inline sampleSize: Int, inline size: Int): Double =
        sampleSize.toDouble / size.toDouble
      def randomSample(sampleSize: Int, size: Int, elements: List[T], acc: List[T]): List[T] =
        if sampleSize == 0 then acc else
          if bernoulliSample(pickProbability(sampleSize, size), generator)
          then randomSample(sampleSize - 1, size -1, elements.tail, elements.head :: acc)
          else randomSample(sampleSize, size - 1, elements.tail, acc)
      optimizedUnsafe(randomSample(size, this.size, elements, Nil), size)

  end Any

  private def optimizedUnsafe[T](reversed: List[T], size: Int): Unsized[T] = size match
    case 0 => Zero
    case 1 =>
      val List(e) = reversed
      One(e)
    case 2 =>
      val List(e2, e1) = reversed
      Two(e1, e2)
    case 3 =>
      val List(e3, e2, e1) = reversed
      Three(e1, e2, e3)
    case 4 =>
      val List(e4, e3, e2, e1) = reversed
      Four(e1, e2, e3, e4)
    case _ => Any(reversed.reverse, size)

end Bag

private inline def bernoulliSample(inline probability: Double, inline generator: Random): Boolean =
  generator.nextDouble() < probability

private def withRepeated[T](v: T, repetitionCount: Int, list: List[T]): List[T] =
  if repetitionCount == 0 then list else withRepeated(v, repetitionCount - 1, v :: list)
