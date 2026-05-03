package util

import util.collections.extensional.FiniteSet
import util.collections.generic.SimpleSet

trait Enumerable[T]:
  /**
    * @return a set that contains all values of type [[T]]
    */
  def values: SimpleSet[T]
end Enumerable

trait FiniteEnumerable[T] extends Enumerable[T]:
  def values: FiniteSet[T]
end FiniteEnumerable
