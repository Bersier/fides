package core.syntax

final case class U() extends V[U]

sealed trait BoolVal extends V[BoolVal]
object True extends BoolVal
object False extends BoolVal

sealed trait Error extends V[Error]
