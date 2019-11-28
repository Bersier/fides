package doodle

import scala.language.implicitConversions
import scala.util.Random

object Evo extends App {

  printTest()

  def printTest(): Unit = {
    def testFunction(a: Double)(x: Array[Double]): Double = {
      -(a * x.length + x.map(xi => xi * xi - a * math.cos(2 * math.Pi * xi)).sum)
    }

    def cauchyNoise(amplitude: Double): Double = {
      amplitude * math.tan(math.Pi * (Random.nextDouble() - .5))
    }

    def mutated(x: (Double, Array[Double])): (Double, Array[Double]) = {
      val (mutationRate, point) = x
      val newMutationRate = math.abs(mutationRate + cauchyNoise(mutationRate / 32))
      val newPoint = point.map(xi => xi + cauchyNoise(mutationRate))
      (newMutationRate, newPoint)
    }

    val initialPopulation = Array.fill(128)((cauchyNoise(1.0 / 16), Array.fill(8)(cauchyNoise(16))))
    val initialFitness: Array[Double] = initialPopulation.map(_._2).map(testFunction(10))

    val champions = evolveToFitness(initialPopulation.zip(initialFitness))(
      target = -1.0 / 16,
      epsilon = 1.0 / 16,
      g => testFunction(10)(g._2),
      mutated
    )

    println()
    for (champion <- champions) {
      println("Fitness: " + champion._2 + ", mutation rate: " + champion._1._1 + ", point: " + champion._1._2.toList)
    }
  }

  var generationCount = 0

  def evolveToFitness[Genome](population: Seq[(Genome, Double)])
                             (implicit target: Double,
                              epsilon: Double,
                              eval: Genome => Double,
                              mutator: Genome => Genome): Seq[(Genome, Double)] = {
    generationCount += 1
    println("Generation " + generationCount + ", Best fitness: " + population.map(_._2).max + ", Worst fitness: " + population.map(_._2).min)
    val worstAcceptableOffspringFitness = population.map(_._2).min + epsilon
    val offspring = population map (_._1) map mutator
    val offspringFitness = offspring map eval
    val viableOffspring = offspring.zip(offspringFitness).filter(_._2 >= worstAcceptableOffspringFitness)
    val champions = viableOffspring.filter(_._2 >= target)
    if (champions.nonEmpty) champions
    else evolveToFitness(
      viableOffspring ++ population.view.sortBy(-_._2).take(population.size - viableOffspring.size)
    )(target, epsilon, eval, mutator)
  }

  trait Target[Genome] extends (Map[Genome, Double] => Option[Target[Genome]])

  def evolveToFitness2[Genome](population: Map[Genome, Double], target: Target[Genome])
                              (implicit eval: Seq[Genome] => Map[Genome, Double],
                               mutator: Iterable[Genome] => Iterable[Genome]): Map[Genome, Double] = {
    ???
  }

//  trait MySeq[+A] extends Iterable[A]
//    with collection.Seq[A]
//    with SeqOps[A, MySeq, MySeq[A]]
//    with IterableFactoryDefaults[A, MySeq] {
//
//    override def iterableFactory: SeqFactory[MySeq] = MySeq
//  }
//  object MySeq extends SeqFactory.Delegate[MySeq](???)

//  class Pairs[+A, +B] private(val firsts: MySeq[A], val seconds: MySeq[B]) extends Seq[(A, B)] with SeqOps[
//      (A, B),
//      ({type P2[Q] =
//        ({type P1[C, D, CD <: (C, D)] = Pairs[C, D]})#P1[_, _, Q] with Seq[Q]
//      })#P2,
//      Pairs[A, B]] {
//
//    override def filter(predicate: ((A, B)) => Boolean): this.type = {
//      ???
//    }
//
//    def map1[C](f: A => C): Pairs[C, B] = Pairs(firsts map f, seconds)
//
//    def map2[D](f: B => D): Pairs[A, D] = Pairs(firsts, seconds map f)
//
//    def pairMap[C, D](f: A => C, g: B => D): Pairs[C, D] = Pairs(firsts map f, seconds map g)
//
//    override def iterator: Iterator[(A, B)] = new Iterator[(A, B)] {
//      private val aIterator = firsts.iterator
//      private val bIterator = seconds.iterator
//      override def hasNext: Boolean = aIterator.hasNext
//      override def next(): (A, B) = (aIterator.next(), bIterator.next())
//    }
//
//    override def apply(i: Int): (A, B) = (firsts(i), seconds(i))
//
//    override def length: Int = firsts.length
//  }
//  object Pairs {
//    def apply[A, B](firsts: MySeq[A], toB: A => B): Pairs[A, B] = new Pairs(firsts, firsts map toB)
//    def apply[A, B](firsts: MySeq[A], seconds: MySeq[B]): Pairs[A, B] = {
//      require(firsts.length == seconds.length)
//      new Pairs(firsts, seconds)
//    }
//    def apply[A, B](pairs: MySeq[(A, B)]): Pairs[A, B] = new Pairs(pairs.map(_._1), pairs.map(_._2))
//  }

  type Eval[A] = A => Double
  type Eval2[A, B] = B => Eval[A]

  // Code below copy-pasted from KellyOptimizer project :S

  trait Sampleable[A] extends Iterator[A] {

    /**
      * Each call to this method returns an independent sample point from the distribution represented by this Sampleable,
      * i.e. calls to this method are i.i.d (by a slight abuse of language).
      */
    def probe(implicit random: Random = Random): A

    final override def hasNext(): Boolean = true

    final override def next(): A = probe
  }

  object Sampleable {
    def from(quantileFunction: Double => Double): Sampleable[Double] = (random: Random) => {
      quantileFunction(random.nextDouble())
    }
  }

  final case class Cauchy(center: Double, gamma: Double) extends Sampleable[Double] {

    override def probe(implicit random: Random): Double = quantile(random.nextDouble())

    def pdf(x: Double): Double = {
      val xr = x - center
      val gamma2 = gamma * gamma
      gamma2 / (math.Pi * gamma * (xr * xr + gamma2))
    }

    def cdf(x: Double): Double = math.atan((x - center) / gamma) / math.Pi + .5

    def quantile(x: Double): Double = center + gamma * math.tan(math.Pi * (x - .5))
  }
}
