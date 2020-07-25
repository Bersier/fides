package simplest

sealed trait Expr
sealed trait Value
sealed trait Service extends Value

final case class C(first: Expr, second: Expr) extends Expr

object Forward extends Service

object Send extends Service

object Replicated extends Expr
object New extends Expr
object Awake extends Expr
object Asleep extends Expr

object U extends Value
object True extends Value
object False extends Value
final class Idee extends Expr

object Pair extends Service
object Bag extends Service

object Signed extends Service

object Merge extends Service
object Contains extends Service
object Equals extends Service
object Branch extends Expr
object Copy extends Expr
object Address extends Expr // Weakens a key to an address. Should this be an inp or an out?

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