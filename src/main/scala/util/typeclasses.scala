package util

import util.collections.extensional.FiniteSet

trait FiniteEnumerable[T]:
  /**
    * @return a set that contains all values of type [[T]]
    */
  def values: FiniteSet[T]
end FiniteEnumerable
