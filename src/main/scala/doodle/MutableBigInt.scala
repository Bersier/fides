package doodle

import java.util

import scala.math.{ScalaNumber, ScalaNumericConversions}

object MainM extends App {
  private val min = Int.MinValue
  println(toBinaryString(min.toLong))
  println(toBinaryString(min >> 31))
  private val m1 = min.toLong + (min >> 31)
  println(toBinaryString(m1))
  println((m1 << 32) + min)
  println(Long.MinValue)
  private val max = Int.MaxValue
  println(((max.toLong + (max >> 31)) << 32) + max)
  println(Long.MaxValue)
  println(-Int.MinValue == Int.MinValue)
  println(-4 % 3)
  println(-3 % 3)
  println(-2 % 3)
  println(-1 % 3)
  println(-0 % 3)
  println(1 % 3)
  println(2 % 3)
  println(3 % 3)
  println(4 % 3)
  println(5 % 3)
  val mutableBigInt = MutableBigInt(3)
  println(mutableBigInt)

  def toBinaryString(long: Long): String = {
    if (long == 0) ""
    else toBinaryString(long >>> 1) + (((long % 2) + 2) % 2).toString
  }
}

object MutableBigInt {
  def apply(int: Int): MutableBigInt = new MutableBigInt(Array(0, int), if (int == 0) 0 else 1)
  def apply(long: Long): MutableBigInt = {
    val i1 = long.toInt
    val i2 = ((long >> 32) + (i1 >>> 31)).toInt
    new MutableBigInt(Array(i2, i1), if (i2 == 0) if (i1 == 0) 0 else 1 else 2)
  }
}

class MutableBigInt private (private var repr: Array[Int], private var size: Int)
  extends ScalaNumber with ScalaNumericConversions with Ordered[MutableBigInt] {

  override def underlying(): MutableBigInt = this

  override def compare(that: MutableBigInt): Int = {
    if (size > that.size) repr(size - 1)
    else if (size < that.size) that.repr(that.size - 1)
    else {
      def helper(i: Int): Int = {
        if (i < 0) 0
        else if (repr(i) > that.repr(i)) 1
        else if (repr(i) < that.repr(i)) -1
        else helper(i - 1)
      }
      helper(size - 1)
    }
  }

  override def isWhole(): Boolean = true

  override def intValue(): Int = repr(0)

  override def longValue(): Long = ((repr(1).toLong + (repr(0) >> 31)) << 32) + repr(0)

  override def floatValue(): Float = ???

  override def doubleValue(): Double = ???

//  def bigIntValue(): BigInt = {
//    val byteArray = Array.ofDim[Byte](math.max())
//    def helper(bigInt: BigInt,)
//  }

  def signum: Int = {
    if (repr(size - 1) > 0) 1
    else if (repr(size - 1) < 0) -1
    else 0
  }

  def + (that: MutableBigInt): MutableBigInt = {
    new MutableBigInt(util.Arrays.copyOf(repr, math.max(size, that.size) << 1), size) += that
  }

  def + (long: Long): MutableBigInt = {
    new MutableBigInt(util.Arrays.copyOf(repr, math.max(size, 2) << 1), size) += long
  }

  def + (int: Int): MutableBigInt = {
    new MutableBigInt(util.Arrays.copyOf(repr, math.max(size, 1) << 1), size) += int
  }

  def - (that: MutableBigInt): MutableBigInt = {
    new MutableBigInt(util.Arrays.copyOf(repr, math.max(size, that.size) << 1), size) -= that
  }

  def - (long: Long): MutableBigInt = {
    new MutableBigInt(util.Arrays.copyOf(repr, math.max(size, 2) << 1), size) -= long
  }

  def - (int: Int): MutableBigInt = {
    new MutableBigInt(util.Arrays.copyOf(repr, math.max(size, 1) << 1), size) -= int
  }

  def += (that: MutableBigInt): this.type = {
    def helper2(i: Int, carry: Int, newSize: Int): this.type = {
      if (carry == 0) {
        size = if (i >= size) newSize else size
        this
      }
      else {
        val s = repr(i).toLong + carry
        repr(i) = s.toInt
        helper2(i + 1, (s >> 32).toInt + (repr(i) >>> 31), if (repr(i) != 0) i + 1 else newSize)
      }
    }
    def helper1(i: Int, carry: Int, newSize: Int): this.type = {
      if (i == that.size) helper2(i, carry, newSize)
      else {
        val s = repr(i).toLong + that.repr(i) + carry
        repr(i) = s.toInt
        helper1(i + 1, (s >> 32).toInt + (repr(i) >>> 31), if (repr(i) != 0) i + 1 else newSize)
      }
    }
    val maxSize = math.max(size, that.size)
    if (repr.length <= maxSize) {
      repr = util.Arrays.copyOf(repr, maxSize << 1)
    }
    helper1(0, 0, 0)
  }

  def += (long: Long): this.type = {
    def helper2(i: Int, carry: Int): this.type = {
      if (carry == 0) {
        size = math.max(size, i)
        this
      }
      else {
        val s = repr(i).toLong + carry
        repr(i) = s.toInt
        helper2(i + 1, (s >> 32).toInt + (repr(i) >>> 31))
      }
    }
    val maxSize = math.max(size, 2)
    if (repr.length <= maxSize) {
      repr = util.Arrays.copyOf(repr, maxSize << 1)
    }
    val long1 = long.toInt
    val s1 = repr(0).toLong + long1
    repr(0) = s1.toInt
    val s2 = repr(1).toLong + (long >> 32) + (long1 >>> 31) + (s1 >> 32) + (repr(0) >>> 31)
    repr(1) = s2.toInt
    helper2(2, (s2 >> 32).toInt + (repr(1) >>> 31))
  }

  def += (int: Int): this.type = {
    def helper2(i: Int, carry: Int): this.type = {
      if (carry == 0) {
        size = math.max(size, i)
        this
      }
      else {
        val s = repr(i).toLong + carry
        repr(i) = s.toInt
        helper2(i + 1, (s >> 32).toInt + (repr(i) >>> 31))
      }
    }
    if (repr.length <= size) {
      repr = util.Arrays.copyOf(repr, size << 1)
    }
    val s = repr(0).toLong + int
    repr(0) = s.toInt
    helper2(1, (s >> 32).toInt + (repr(0) >>> 31))
  }

  def -= (that: MutableBigInt): this.type = {
    def helper2(i: Int, carry: Int): this.type = {
      if (carry == 0) {
        size = math.max(size, i)
        this
      }
      else {
        val s = repr(i).toLong + carry
        repr(i) = s.toInt
        helper2(i + 1, (s >> 32).toInt + (repr(i) >>> 31))
      }
    }
    def helper1(i: Int, carry: Int): this.type = {
      if (i == that.size) helper2(i, carry)
      else {
        val s = repr(i).toLong - that.repr(i) + carry
        repr(i) = s.toInt
        helper1(i + 1, (s >> 32).toInt + (repr(i) >>> 31))
      }
    }
    val maxSize = math.max(size, that.size)
    if (repr.length <= maxSize) {
      repr = util.Arrays.copyOf(repr, maxSize << 1)
    }
    helper1(0, 0)
  }

  def -= (long: Long): this.type = {
    def helper2(i: Int, carry: Int): this.type = {
      if (carry == 0) {
        size = math.max(size, i)
        this
      }
      else {
        val s = repr(i).toLong + carry
        repr(i) = s.toInt
        helper2(i + 1, (s >> 32).toInt + (repr(i) >>> 31))
      }
    }
    val maxSize = math.max(size, 2)
    if (repr.length <= maxSize) {
      repr = util.Arrays.copyOf(repr, maxSize << 1)
    }
    val long1 = long.toInt
    val s1 = repr(0).toLong - long1
    repr(0) = s1.toInt
    val s2 = repr(1).toLong - (long >> 32) - (long1 >>> 31) + (s1 >> 32) + (repr(0) >>> 31)
    repr(1) = s2.toInt
    helper2(2, (s2 >> 32).toInt + (repr(1) >>> 31))
  }

  def -= (int: Int): this.type = {
    if (int == Int.MinValue) += (-int.toLong)
    else += (-int)
  }
}
