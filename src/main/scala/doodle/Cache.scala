package doodle

import scala.collection.mutable

abstract class Cache[K, V](size: Int)(value: (K, V) => Double) extends mutable.Map[K, V] {
  private[this] val internalMap = mutable.Map.empty[K, (V, Long)]

  private[this] def negativeWeight(k: K): Double = {
    val (v, t) = internalMap(k)
    - value(k, v) * t
  }

  private[this] def cleanup(): Unit = {
    val pq = mutable.PriorityQueue[K]()(Ordering.by(negativeWeight))
    for (k <- internalMap.keys) {
      pq += k
    }
    for (_ <- 0 until size/2) {
      val k = pq.dequeue()
      internalMap -= k
    }
    for ((k, (v, _)) <- internalMap) {
      internalMap(k) = (v, 0)
    }
  }

//  override def +=(kv: (K, V)): Cache.this.type = {
//    if (!internalMap.contains(kv._1)) {
//      internalMap(kv._1) =(kv._2, 0)
//      if (internalMap.size >= size) {
//        cleanup()
//      }
//    }
//    this
//  }

//  override def -=(key: K): Cache.this.type = {
//    internalMap -= key
//    this
//  }

  override def get(key: K): Option[V] = {
    internalMap.get(key).map({case (v, t) => internalMap(key) = (v, t + 1); v })
  }

  override def iterator: Iterator[(K, V)] = internalMap.iterator.map({case (k, (v, t)) => (k, v)})
}