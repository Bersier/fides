package simplest

sealed trait Expr

final case class C(first: Expr, second: Expr) extends Expr

object Forward extends Expr

object Send extends Expr

object Replicated extends Expr
object New extends Expr
object Awake extends Expr
object Asleep extends Expr

object U extends Expr
object True extends Expr
object False extends Expr
final class Idee extends Expr

object Pair extends Expr
object Bag extends Expr

object Signed extends Expr

object Merge extends Expr
object Contains extends Expr
object Equals extends Expr
object Branch extends Expr
object Copy extends Expr
object Public extends Expr

object Broadcast extends Expr

object Start extends Expr
object Pause extends Expr
object Dissolve extends Expr
object Kill extends Expr
object Move extends Expr

object Code extends Expr
object Eval extends Expr
object AsCode extends Expr
object CodeZip extends Expr
object Escape extends Expr

// (Send, (Merge, (APair, (A, (APair (B, (Code, C)))))))
// (Forward, ((Merge, (A, B)), C))
// (Forward, (Merge, (A, B)))