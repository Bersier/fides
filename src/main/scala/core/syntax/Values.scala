package core.syntax

final case class U() extends A with V[U]

sealed trait BoolVal extends A with V[BoolVal]
object True extends BoolVal
object False extends BoolVal

sealed trait Error extends A with V[Error]
