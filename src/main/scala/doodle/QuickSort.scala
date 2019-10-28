package doodle

import scala.util.Random

object QuickSort extends App {

  var switchCount = 0L
  // List(-6230264942289170105, 282539593673290554, 9082412028958097421, 4212809111023359879, -5261421210620699272)
  // List(558128293979975034, 3256095575263822714, 8128179335914701906)
  val array = Array.fill(1 << 27)(Random.nextLong())
  //  println(array.toList)
//  shuffle(array)
//  println(array.toList)
  sort(array)
//  println(array.toList)
  println(isSorted(array))

  def sort(array: Array[Long]): Unit = {
    shuffle(array)
    println("Start sorting per se")
    def sort(from: Int, to: Int)(implicit array: Array[Long]): Unit = to - from match {
      case 0 => ()
      case 1 => if (array(from) > array(to)) switch(from, to)
      case _ => {
        val pivot = medianOfThree(array(from), array(from + 1), array(from + 2))
        val transitionIndex = partition(from, to, pivot)
        sort(from, transitionIndex - 1)
        sort(transitionIndex, to)
      }
    }
    sort(0, array.length - 1)(array)
    println(switchCount)
  }

  def shuffle(array: Array[Long], randomInt: Int => Int = Random.nextInt): Unit = for (i <- array.indices) {
    switch(i, randomInt(array.length - i) + i)(array)
  }

  @inline
  def medianOfThree(i1: Long, i2: Long, i3: Long): Long = {
    if (i1 < i2) {
      if (i2 <= i3) i2
      else {
        if (i1 <= i3) i3
        else i1
      }
    }
    else if (i1 == i2) i1
    else {
      if (i2 >= i3) i2
      else {
        if (i1 <= i3) i1
        else i3
      }
    }
  }

  def partition(from: Int, to: Int, pivot: Long)(implicit array: Array[Long]): Int = {
    if (from == to) {
      if (array(from) <= pivot) from + 1
      else from
    }
    else {
      if (array(from) > pivot) {
        switch(from, to)
        partition(from, to - 1, pivot)
      }
      else partition(from + 1, to, pivot)
    }
  }

  @inline
  def switch(i: Int, j: Int)(implicit array: Array[Long]): Unit = {
    switchCount += 1
    val oldIValue = array(i)
    array(i) = array(j)
    array(j) = oldIValue
  }

  def isSorted(array: Array[Long]): Boolean = {
    def loop(i: Int): Boolean = i == 0 || array(i - 1) <= array(i) && loop(i - 1)
    loop(array.length - 1)
  }
}
