package core.syntax

final case class U() extends A with V[U]

sealed trait SimT[T] extends A

final case class Z(value: BigInt) extends SimT[BigInt] with V[Z]

sealed trait BoolVal extends SimT[Boolean] with V[BoolVal]
object True extends BoolVal
object False extends BoolVal

final class Atom extends A with V[Atom]

final case class ScalaVal[T](value: T) extends A with V[ScalaVal[T]]
