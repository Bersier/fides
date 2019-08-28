package doodle.Collatz

final class CollatzerLengthAndHeight(e: Int) {
  private[this] val c: Int = 1 << e
  private[this] val a: Array[Long] = Array.ofDim[Long](c)
  private[this] val b: Array[Long] = Array.ofDim[Long](c)
  private[this] val l: Array[Int] = Array.ofDim[Int](c)
  private[this] val ha: Array[Long] = Array.ofDim[Long](c)
  private[this] val hb: Array[Long] = Array.ofDim[Long](c)

  private[this] val sl: Array[Int] = Array.ofDim[Int](c)
  private[this] val sh: Array[Long] = Array.ofDim[Long](c)

  def lengthAndHeight(n: Long): (Int, Long) = {
    def lengthAndHeight(n: Long, l: Int, h: Long): (Int, Long) = {
      val m = n >> e; val i = (n % c).toInt
      if (m == 0) (l + sl(i), h.max(sh(i)))
      else lengthAndHeight(a(i)*m + b(i), l + this.l(i), h.max(ha(i)*m + hb(i)))
    }
    lengthAndHeight(n, 0, n)
  }

//  for (i <- 1 until c) {
//    val (l, h) = Collatz.lengthAndHeight8(i)
//    sl(i) = l; sh(i) = h
//  }

  for (i <- 0 until c) {
    val (ai, bi, li, hai, hbi) = compute(c, i, 0, c, i)
    println()
    a(i) = ai; b(i) = bi; l(i) = li; ha(i) = hai; hb(i) = hbi
  }

  private[this] def compute(a: Long, b: Long, l: Int, ha: Long, hb: Long): (Long, Long, Int, Long, Long) = {
    print(a + "m + " + b + " -> ")
    if (a % 2 == 1) (a, b, l, ha, hb)
    else if (b % 2 == 0) compute(a/2, b/2, l + 1, ha, hb)
    else {
      val nextA = 3 * a
      val nextB = 3 * b + 1
      if (nextA > ha && nextB < hb || nextB > hb && nextA < ha) throw new AssertionError("Equivocal Max")
      compute(nextA/2, nextB/2, l + 2, nextA.max(ha), nextB.max(hb))
    }
  }

  override def toString: String = {
    val builder = new StringBuilder
    for (i <- 0 until c) {
      builder.append(toString(i))
    }
    builder.toString()
  }

  def toString(i: Int): String = {
    c + "m + " + i + " -> " + a(i) + "m + " + b(i) +
      "; l = " + l(i) + "; max = " + ha(i) + "m + " + hb(i) + "\n"
  }
}
