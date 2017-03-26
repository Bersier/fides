package doodle.Collatz

import doodle.Cache

import scala.annotation.switch
import scala.collection.mutable

object Collatz extends App {

  val mem: mutable.Map[Long, (Int, Long)] = new Cache[Long, (Int, Long)](1000000)({
      case (_, (l, h)) => l * math.log(h)
    }).withDefaultValue((0, 1))

  def lengthAndHeight2(n: Long): (Int, Long) = {
    def lengthAndHeight(n: Long, l: Int, h: Long): (Int, Long) = {
      if (n == 2) (l, h)
      else {
        val m = n >> 1
        ((n % 2).toInt: @switch) match {
          case 0 => lengthAndHeight(    m    , l + 1, h               )
          case _ => lengthAndHeight(3 * m + 2, l + 2, h.max(6 * m + 4))
        }
      }
    }
    lengthAndHeight(n, 0, n)
  }

  def lengthAndHeight4(n: Long): (Int, Long) = {
    def lengthAndHeight(n: Long, l: Int, h: Long): (Int, Long) = {
      if (n < 3) (n.toInt: @switch) match {
        case 1 => (l, h)
        case _ => (l + 1, h)
      }
      else {
        val m = n >> 2
        ((n % 4).toInt: @switch) match {
          case 0 => lengthAndHeight(    m    , l + 2, h                 )
          case 1 => lengthAndHeight(3 * m + 1, l + 3, h.max(12 * m + 4 ))
          case 2 => lengthAndHeight(3 * m + 2, l + 3, h.max(6  * m + 4 ))
          case 3 => lengthAndHeight(9 * m + 8, l + 4, h.max(18 * m + 16))
        }
      }
    }
    lengthAndHeight(n, 0, n)
  }

  def lengthAndHeight8(n: Long): (Int, Long) = {
    def lengthAndHeight(n: Long, l: Int, h: Long): (Int, Long) = {
      if (n < 5) (n.toInt: @switch) match {
        case 1 => (l    , h        )
        case 2 => (l + 1, h        )
        case 3 => (l + 7, h.max(16))
        case 4 => (l + 2, h        )
      }
      else {
        val m = n >> 3
        ((n % 8).toInt: @switch) match {
          case 0 => lengthAndHeight(   m     , l + 3, h               )
          case 1 => lengthAndHeight(9 *m + 2 , l + 5, h.max(24*m + 4 ))
          case 2 => lengthAndHeight(3 *m + 1 , l + 4, h.max(12*m + 4 ))
          case 3 => lengthAndHeight(9 *m + 4 , l + 5, h.max(36*m + 16))
          case 4 => lengthAndHeight(3 *m + 2 , l + 4, h               )
          case 5 => lengthAndHeight(3 *m + 2 , l + 4, h.max(24*m + 16))
          case 6 => lengthAndHeight(9 *m + 8 , l + 5, h.max(18*m + 16))
          case 7 => lengthAndHeight(27*m + 26, l + 6, h.max(54*m + 52))
        }
      }
    }
    lengthAndHeight(n, 0, n)
  }

new CollatzerLengthAndHeight(5)

//  val collatzer = new CollatzerLengthAndHeight(16)
//  var rl = 0
//  var rh: Long = 1
//  var i = 1: Long
//  while (true) {
//    val (l, h) = collatzer.lengthAndHeight(i)
//    if (h > rh) {
//      rl = l.max(rl)
//      rh = h.max(rh)
//      println("" + i + ": " + l + ", " + h)
//    }
//    i += 1
//  }
}


//def lengthAndHeight(n: Long): (Int, Long) = {
//  def lengthAndHeight(n: Long, l: Int, h: Long): (Int, Long) = {
//  if (mem.contains(n)) {
//     val (nl, nh) = mem(n)
//     (l + nl, h.max(nh))
//   }
//   else (n: @switch) match {
//  case 1 => (l, h)
//  case 2 => (l + 1, h)
//  case _ => ((n % 4).toInt: @switch) match {
//  case 0 => lengthAndHeight(n >> 2, l + 2, h)
//  case 1 => {
//  val m = (n - 1) >> 2
//  lengthAndHeight(3 * m + 1, l + 3, h.max(12 * m + 4))
//}
//  case 2 => {
//  val m = (n - 2) >> 2
//  lengthAndHeight(3 * m + 2, l + 3, h.max(6 * m + 4))
//}
//  case _ => {
//  val m = (n - 3) >> 2
//  lengthAndHeight(9 * m + 8, l + 4, h.max(18 * m + 16))
//}
//}
//}
//}
//  def lengthAndHeightMem(n: Long, s: List[Long]): (Int, Long) = {
//    def end(lh: (Int, Long)) = {
//      var (l, h) = lh
//      var f = n
//      for (i <- s) {
//        h = h.max(i)
//        l += 1
//        mem(i) = (l, h)
//        f = i
//      }
//      mem(f)
//    }
//    if (mem.contains(n)) end(mem(n))
//    else if (n == 1) end((0, 1))
//    else if (n % 2 == 0) lengthAndHeightMem(n / 2, n::s)
//    else lengthAndHeightMem(3 * n + 1, n::s)
//  }
//  if (math.random < 0.01) lengthAndHeightMem(n, Nil)
//  else lengthAndHeight(n, 0, n)
//}