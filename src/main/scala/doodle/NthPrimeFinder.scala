package doodle

import scala.collection.mutable

object NthPrimeFinder extends App {

  val n = 1000000000
  println(n + "th prime = " + getNthPrime(n))

  def getPrimesUpToNSlow(n: Int): Vector[Int] = {
    if (n < 2) {
      Vector()
    }
    else {
      def loop(i: Int): Vector[Int] = {
        if (i == n) getPrimesUpToNSlow(n - 1) :+ n
        else if (n % i == 0) getPrimesUpToNSlow(n - 1)
        else loop(i + 1)
      }
      loop(2)
    }
  }

  def getNthPrime2(n: Long): Long = {
    if (n < 17) Array(1, 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53)(n.toInt)
    else {
      val bound = math.sqrt(findBound(n)).toInt
      val primes = getPrimesUpTo(bound)
      val start = 1

      var index: Long = primes.length
      var previousOffset = 0
      var offset = primes.last + 2
      var end = 2
      var chunk: mutable.BitSet = null
      do {
        val chunkSize = math.sqrt(offset).toInt
        val nextOffset = offset + 2*chunkSize
        while (end < primes.length && primes(end) < math.sqrt(nextOffset - 1)) {
          end += 1
        }
        chunk = mutable.BitSet.empty ++ (0 until chunkSize)
        remove(offset, ArraySlice(primes).slice(start, end), chunkSize)(chunk)
        index += chunk.size
        previousOffset = offset
        offset = nextOffset
      } while (index < n)

      previousOffset + 2*chunk.toIndexedSeq((n - index).toInt + chunk.size - 1).toLong
    }
  }

  def getNthPrime(n: Long): Long = {
    if (n < 17) Array(1, 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53)(n.toInt)
    else {
      val bound = math.sqrt(findBound(n)).toInt
      val primes = getPrimesUpTo(bound)
      val start = 1

      var index: Long = primes.length
      var previousOffset: Long = 0
      var offset: Long = primes.last + 2
      var end = 2
      var chunk: JumpList = null
      do {
        val chunkSize = math.sqrt(offset).toInt
        val nextOffset = offset + 2*chunkSize
        while (end < primes.length && primes(end) < math.sqrt(nextOffset - 1)) {
          end += 1
        }
        chunk = new JumpList(chunkSize)
        remove(offset, ArraySlice(primes).slice(start, end))(chunk)
        index += chunk.size
        previousOffset = offset
        offset = nextOffset
      } while (index < n)

      previousOffset + 2*chunk.toIndexedSeq((n - index).toInt + chunk.size - 1).toLong
    }
  }

  def getPrimesUpTo(bound: Int): Array[Int] = {
    if (bound < 16) {
      Array(2,3,5,7,11,13).takeWhile(_ <= bound)
    }
    else {
      val bootstrapper = getPrimesUpTo(math.sqrt(bound).toInt)
      val offset: Int = bootstrapper.last + 2
      val remainder: JumpList = new JumpList((bound - offset + 1) / 2)
      remove(offset, ArraySlice(bootstrapper).tail)(remainder)
      bootstrapper ++ remainder.map(2 * _ + offset)
    }
  }

  def findBound(n: Long): Long = {
    def loop(x: Long, fx: Double): Long = {
      if (x >= fx) x
      else {
        val next = fx.ceil.toLong
        loop(fx.ceil.toLong, n*(math.log(next) + 2))
      }
    }
    loop(0, n)
  }

  def remove(offset: Long, ps: Iterable[Int])(implicit list: JumpList): Unit = {
    for (p <- ps) {
      remove(offset, p)
    }
  }

  def remove(offset: Long, ps: Iterable[Int], size: Int)(implicit list: mutable.BitSet): Unit = {
    for (p <- ps) {
      remove(offset, p, size)
    }
  }

  def remove(offset: Long, p: Int)(implicit list: JumpList): Unit = {
    var current = math.max((4*p - (offset % (2*p) + p)) % (2*p), p*p - offset).toInt / 2
    while (current < list.length) {
      list.remove(current)
      current += p
    }
  }

  def remove(offset: Long, p: Int, size: Int)(implicit list: mutable.BitSet): Unit = {
    var current = math.max((4*p - (offset % (2*p) + p)) % (2*p), p*p - offset).toInt / 2
    while (current < size) {
      list.remove(current)
      current += p
    }
  }

  def countFrom(start: Long): Iterator[Long] = {
    new Iterator[Long] {
      var i = start - 1
      override def hasNext: Boolean = true

      override def next(): Long = {
        i += 1
        i
      }
    }
  }
}


