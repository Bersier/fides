//package doodle.Collatz
//
//object Main extends App {
//  var platzer: CollatzerPlatzer = CollatzerPlatzer1
//  for (_ <- 2 to 29) {
//    platzer = new CollatzerPlatzerE(platzer)
//    println("e = " + platzer.e + ", size = " + platzer.size + ", speedup = " + platzer.twoToTheE / platzer.size)
//  }
//  val collatzer = new Collatzer(20)
//  for (i <- platzer.dropWhile(_ < (1 << 20))) {
//    if (!collatzer.holdsFor(i)) {
//      println("Collatz conjecture disproved. n = " + i)
//    }
//  }
//  var m: Long = 1
//  while (true) {
//    println("n = " + m*platzer.twoToTheE)
//    for (i <- platzer) {
//      if (!collatzer.holdsFor(m*platzer.twoToTheE + i)) {
//        println("Collatz conjecture disproved. n = " + m*platzer.twoToTheE + i)
//      }
//    }
//    m += 1
//  }
//}
//
//trait CollatzerPlatzer extends Traversable[Int] {
//  def e: Int
//  def twoToTheE: Int
//  def memSize: Int
//}
//
//object CollatzerPlatzer1 extends CollatzerPlatzer {
//  override def foreach[U](f: (Int) => U): Unit = f(1)
//  override def e: Int = 1
//  override def twoToTheE: Int = 2
//  override def memSize: Int = 0
//}
//
//final class CollatzerPlatzerE(predecessor: CollatzerPlatzer) extends CollatzerPlatzer {
//  override val e :Int = predecessor.e + 1
//  override val twoToTheE: Int = 1 << e
//  private[this] val mem: Array[Int] = Array.ofDim(predecessor.size << 1)
//  private[this] var counter = 0
//
//  for (i <- predecessor if noShrinkageAt(i)) { addToMem(i) }
//  for (i <- predecessor) {
//    val trueI = i + predecessor.twoToTheE
//    if (noShrinkageAt(trueI)) addToMem(trueI)
//  }
//  println(memSize.toDouble / predecessor.memSize)
//
//  override def foreach[U](f: (Int) => U): Unit = for (i <- 0 until counter) { f(mem(i)) }
//  override def memSize: Int = counter
//
//  private[this] def addToMem(n: Int): Unit = {
//    mem(counter) = n
//    counter += 1
//  }
//
//  private[this] def noShrinkageAt(bInitial: Int): Boolean = {
//    def helper(a: Long, b: Long): Boolean = {
//      if (a < twoToTheE) {
//        if (b > bInitial) throw new AssertionError("Equivocal Min")
//        false
//      }
//      else if (a % 2 == 1) true
//      else if (b % 2 == 0) helper(a >> 1, b >> 1)
//      else helper((3 * a) >> 1, (3 * b + 1) >> 1)
//    }
//    helper(twoToTheE, bInitial)
//  }
//}
