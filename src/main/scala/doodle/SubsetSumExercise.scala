package doodle

import scala.collection.mutable
import scala.math.abs

// TODO combine best of solution 1 and 2
// TODO greedy can be repeated until a fixed point is reached
object SubsetSumExercise extends App {
  type SeqN3Int = (Seq[Int], Int, Int, Int)
  type SeqNInt  = (Seq[Int], Int)

  private val elements: Seq[Int] = Seq(142, 753, 764, 952, 123, 234, 444, 783, 725, 328, 123, 12)
  private val target = 3000
  println(solution1(elements, target))
  println(solution2(elements, target))

  def solution1(elements: Seq[Int], k: Int): SeqNInt = {
    val memory = mutable.Map.empty[SeqNInt, SeqN3Int]
    def addAndReturn(key: SeqNInt, value: SeqN3Int): SeqNInt = {
      memory += key -> value
      (value._1, value._2)
    }
    var counter = 0
    def rec(elements: Seq[Int], k: Int, min: Int, max: Int): SeqNInt = {
      counter += 1
      if      (max <= k) (elements.filter(_ > 0), max)
      else if (min >= k) (elements.filter(_ < 0), min)
      else {
        val head +: tail = elements
        val option = memory.get((elements, k))
        lazy val (memS, memK, memMin, memMax) = option.get
        if (option.isDefined && memMin <= min && memMax >= max) (memS, memK)
        else {
          val (newMin, newMax) = if (head < 0) (min - head, max) else (min, max - head)
          val (s1, k1) = rec(tail, k, newMin, newMax)
          val diff1 = abs(k1 - k)
          if (diff1 == 0 || head == 0) (s1, k1)
          else {
            val (s2MinusHead, k2MinusHead) = rec(tail, k - head, newMin, newMax)
            val (s2, k2) = (head +: s2MinusHead, k2MinusHead + head)
            val diff2 = abs(k2 - k)
            addAndReturn((elements, k), if (diff1 <= diff2) (s1, k1, min, max) else (s2, k2, min, max))
          }
        }
      }
    }
    val result = rec(elements.sortWith(abs(_) > abs(_)), k, elements.filter(_ < 0).sum, elements.filter(_ > 0).sum)
    println("count: " + counter)
    result
  }

  def solution2(elements: Seq[Int], k: Int): SeqNInt = {
    var counter = 0
    val greedySolution = greedy(elements.sortWith(abs(_) > abs(_)), k)
    val upperBound: Int = abs(k - greedySolution._2)

    //noinspection RedundantBlock
    def rec(elements: Seq[Int], k: Int, min: Int, max: Int): Seq[SeqNInt] = {
      counter += 1
      if (max <= k) {
        if (k - max >= upperBound) Seq()
        else Seq((elements.filter(_ > 0), max))
      }
      else if (min >= k) {
        if (min - k >= upperBound) Seq()
        else Seq((elements.filter(_ < 0), min))
      }
      else elements match {
        case Seq() => Seq((Seq(), 0))
        case head +: tail => {
          val (newMin, newMax) = if (head < 0) (min - head, max) else (min, max - head)
          val seq1 = rec(tail, k, newMin, newMax)
          val seq2 = for ((s, k) <- rec(tail, k - head, newMin, newMax)) yield (head +: s, k + head)
          merge(seq1, seq2)(Ordering.by(_._2))
        }
      }
    }

    val negative = elements.filter(_ < 0)
    val positive = elements.filter(_ > 0)
    val max = positive.sum
    val min = negative.sum

    val groups = elements.sorted.reverse.grouped(math.ceil(elements.size / 2.0).toInt)

    val s1 = rec(groups.next, k, min, max)
    val s2 = rec(groups.next, k, min, max)

    println("count: " + counter)
    closestPair(s2, s1.reverse, k, greedySolution)
  }

  //noinspection RedundantBlock
  @scala.annotation.tailrec
  def greedy(elements: Seq[Int], k: Int, acc: Seq[Int] = Seq()): SeqNInt = elements match {
    case Seq() => (acc, acc.sum)
    case head +: tail => {
      if (abs(k - head) < abs(k)) greedy(tail, k - head, head +: acc)
      else greedy(tail, k, acc)
    }
  }

  //noinspection RedundantBlock
  @scala.annotation.tailrec
  def closestPair(seq1: Seq[SeqNInt], seq2: Seq[SeqNInt], k: Int, bestSoFar: SeqNInt): SeqNInt = {
    seq1 match {
      case Seq() => bestSoFar
      case head1 +: tail1 => seq2 match {
        case Seq() => bestSoFar
        case head2 +: tail2 => {
          val sum = head1._2 + head2._2
          val newBestSoFar = if (abs(sum - k) < abs(bestSoFar._2 - k)) (head1._1 ++ head2._1, sum) else bestSoFar
          if (sum == k) (head1._1 ++ head2._1, sum)
          else if (sum < k) closestPair(tail1, seq2, k, newBestSoFar)
          else              closestPair(seq1, tail2, k, newBestSoFar)
        }
      }
    }
  }

  //noinspection RedundantBlock
  @scala.annotation.tailrec
  def merge[A: Ordering](seq1: Seq[A], seq2: Seq[A], acc: Seq[A] = Seq()): Seq[A] = seq1 match {
    case Seq() => acc.reverse ++ seq2
    case head1 +: tail1 => seq2 match {
      case Seq() => acc.reverse ++ seq1
      case head2 +: tail2 => {
        val ord = implicitly[Ordering[A]]; import ord._
        if (head2 < head1) merge(seq1, tail2, head2 +: acc)
        else               merge(tail1, seq2, head1 +: acc)
      }
    }
  }
}
