//package doodle
//
//final class DSet[A] private(private[this] val delegate: Set[A]) extends Set[A] {
//
//  override def iterator: Iterator[A] = delegate.iterator
//
//  override def contains(elem: A): Boolean = delegate.contains(elem)
//
//  override def +(elem: A): DSet[A] = new DSet(delegate + elem)
//
//  override def -(elem: A): DSet[A] = new DSet(delegate - elem)
//
//  final case class T(a: A)
//}
//
//object DSet {
//
//  def apply[A](as: A*): DSet[A] = new DSet(Set(as: _*))
//
//  implicit def unwrap[A](t: DSet[A]#T): A = t.a
//
//  implicit def as[A, B <: DSet[A]#T](set: DSet[B]): DSet[A] = new DSet(set.map(unwrap))
//}
//
//object test {
//  def getSubset[A](set: DSet[A]): DSet[set.T] = ???
//
//  val s1: DSet[Int] = ???
//
//  val s2: DSet[Int] = getSubset(s1)
//
//  val s3: DSet[s1.T] = ???
//}