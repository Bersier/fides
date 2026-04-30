package util

import util.collections.extensional.FiniteSet
import util.collections.generic.SimpleSet

trait FiniteEnumerable[T]:
  /**
    * @return a set that contains all values of type [[T]]
    */
  def values: FiniteSet[T]
end FiniteEnumerable
