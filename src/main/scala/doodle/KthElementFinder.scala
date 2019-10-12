package doodle

object KthElementFinder extends App {
  private val chunkSize = 5

  println(kthSmallest(Seq(1, 2, 7, 4, 9, 8, 3, 0), 3)(implicitly[Ordering[Int]], findPivot))

  @specialized(Int, Long)
  @scala.annotation.tailrec
  def kthSmallest[A : Ordering](seq: Seq[A], k: Int)(implicit pivotFinder: Seq[A] => A): A = {
    require(0 <= k && k < seq.size)

    if (seq.length == 1) seq(0)
    else {
      val pivot = pivotFinder(seq)
      val (smaller, larger) = partition(seq, pivot)
      if      (smaller.size < k) kthSmallest(larger, k - 1 - smaller.size)
      else if (smaller.size > k) kthSmallest(smaller, k)
      else /* (smaller.size == k) */ pivot
    }
  }

  @specialized(Int, Long)
  def quickSort[A : Ordering](seq: Seq[A])(implicit pivotFinder: Seq[A] => A): Seq[A] = {
    if (seq.length <= 1) seq
    else {
      val pivot = pivotFinder(seq)
      val (smaller, larger) = partition(seq, pivot)
      quickSort(smaller) ++ (pivot +: quickSort(larger))
    }
  }

  @specialized(Int, Long)
  def partition[A : Ordering](seq: Seq[A], pivot: A): (Seq[A], Seq[A]) = {
    val ord = implicitly[Ordering[A]]; import ord._

    //noinspection RedundantBlock
    @scala.annotation.tailrec
    def loop(seq: Seq[A], smaller: Seq[A], larger: Seq[A]): (Seq[A], Seq[A]) = seq match {
      case _ if seq.isEmpty => (smaller, larger)
      case head +: tail   => {
        if      (head < pivot) loop(tail, head +: smaller,         larger)
        else if (head > pivot) loop(tail,         smaller, head +: larger)
        else /* (head == pivot) */ {
          val (s, l) = tail.partition(_ < pivot)
          (s ++ smaller, l ++ larger)
        }
      }
    }

    loop(seq, Seq.empty, Seq.empty)
  }

  @specialized(Int, Long)
  def findPivot[A : Ordering](seq: Seq[A]): A = {
    require(seq.nonEmpty)

    val medians: Seq[A] = (for (chunk <- seq.grouped(chunkSize)) yield median(chunk)).toSeq
    kthSmallest(medians, medians.size / 2)(implicitly[Ordering[A]], findPivot)
  }

  private def median[A : Ordering](seq: Seq[A]): A = (seq.sorted: Seq[A])(seq.size / 2)
}
