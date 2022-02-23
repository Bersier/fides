package core

sealed trait Expr derives CanEqual
sealed trait Value extends Expr
sealed trait Service extends Value

final case class C(first: Expr, second: Expr) extends Expr

object Forward extends Service // Could be used to represent messages as well

object Send extends Service // Replace by InOut? (InOut cInp) // Becomes an out
object Launch extends Expr
object Catch extends Expr

object Mem extends Expr
object Read extends Expr
object CAS extends Expr // ...

object Concurrent extends Expr
object Replicated extends Expr
object New extends Expr
object Shadow extends Expr // Shadows a location, so that a local interpretation can be used
object Awake extends Expr
object Asleep extends Expr

object U extends Value
object True extends Value
object False extends Value
final class Idee extends Expr

object Pair extends Service

object Signed extends Service

object Equals extends Service
// object Branch extends Expr // Branch(V, .) <-> Pair(True, V) | Branch(., V) <-> Pair(False, V)
object Copy extends Expr
object Address extends Expr // Weakens a key to an address. Should this be an inp or an out?

object Broadcast extends Expr

// These should return a status: ((Start <process_name>) resultOut)
object Start extends Expr
object Pause extends Expr
object Kill extends Expr
// Maybe these should be optional handles that can be added to a scope?

object Code extends Expr
object Eval extends Expr
object AsCode extends Expr
object CodeZip extends Expr
object Escape extends Expr

// (Send, (Merge, (APair, (A, (APair (B, (Code, C)))))))
// (Forward, ((Merge, (A, B)), C))
// (Forward, (Merge, (A, B)))
