package doodle

import scala.collection.mutable

def wordOccurences[A, B](word: IndexedSeq[A], text: IndexedSeq[B])(using CanEqual[A, B]): IndexedSeq[Int] =
  // Computing prefixLengths in linear time is very similar to wordOccurences; it seems almost as hard...
  // Can it be bootstrapped recursively somehow?
  val prefixLengths: Array[Int] = ???
  assert(prefixLengths.length == word.length)
  val occurenceIndices = new mutable.ArrayBuilder.ofInt
  def loopOverText(textI: Int, wordI: Int): Unit = if textI >= text.length - word.length then () else
    // During this loop, progress is made on the compared indices of the text (each index gets checked only once)
    def computedMatchLength(i: Int): Int =
      val lengthCandidate = i - textI + wordI
      assert(lengthCandidate <= word.length)
      if lengthCandidate == word.length || word(lengthCandidate) != text(i)
      then lengthCandidate
      else computedMatchLength(i + 1)
    val matchLength = computedMatchLength(textI)
    assert(matchLength >= 0)
    if matchLength == word.length then occurenceIndices.addOne(textI)
    // During this loop, progress is made on the checked start indices (each index gets considered only once)
    def nextIndices(shiftCandidate: Int): (Int, Int) =
      // val wordStartI = (textI - wordI) + shiftCandidate
      val targetMatchLength = matchLength - shiftCandidate
      assert(targetMatchLength >= -1)
      if targetMatchLength < 0 then (textI + matchLength + 1, 0)
      else if prefixLengths(shiftCandidate) == targetMatchLength then (textI + matchLength, targetMatchLength)
      else nextIndices(shiftCandidate + 1)
    val (nextI, nextWordI) = nextIndices(1)
    loopOverText(nextI, nextWordI)
  loopOverText(0, 0)
  assert(occurenceIndices.length <= text.length + 1)
  occurenceIndices.result()
