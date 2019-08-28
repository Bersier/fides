package doodle

import scala.collection.mutable

final abstract class LongMap[Value](initialCapacity: Int) extends scala.collection.mutable.Map[Long, Value] {
  private[this] val fallback: mutable.Map[Long, Value] = mutable.HashMap.empty
  private[this] val occupied: mutable.BitSet = mutable.BitSet.empty

  private[this] var keys: Array[Long] = ??? // Array.ofDim[Long](initialCapacity)
  private[this] var values: Array[Value] = ??? // Array.ofDim[Value](initialCapacity)

//  override def +=(kv: (Long, Value)): LongMap.this.type = kv match {
//    case (key, value) =>
//      val i: Int = index(key)
//      if (!occupied(i)) {
//        keys(i) = key
//        values(i) = value
//        occupied.add(i)
//      }
//      else if (keys(i) == key) {
//        values(i) = value
//      }
//      else {
//        fallback += kv
//      }
//      this
//  }

//  override def -=(key: Long): LongMap.this.type = {
//    val i: Int = index(key)
//    if (occupied(i)) {
//      if (keys(i) == key) {
//        values(i) = _: Value
//        occupied.remove(i)
//        if (fallback.size > occupied.size / 2 + 8) {
//
//        }
//      }
//      else {
//        fallback -= key
//      }
//    }
//    else {
//      fallback -= key
//    }
//    this
//  }

  override def get(key: Long): Option[Value] = ???

  override def iterator: Iterator[(Long, Value)] = ???

  private[this] def index(key: Long): Int = {
    ???
  }
}
