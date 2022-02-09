package museum.simple.syntax

trait Top
trait Proc extends Top
trait Expr extends Top
trait Pot extends Proc with Expr

final case class Send(inp: Expr, address: Expr) extends Proc
final case class Forward(inp: Expr, out: Expr) extends Proc
final case class Replicated(process: Proc) extends Proc
final case class New(idees: Expr, process: Proc) extends Proc
final case class Awake(name: Expr, process: Proc) extends Proc
final case class Asleep(name: Expr, process: Proc) extends Proc

object U extends Expr
object True extends Expr
object False extends Expr
final class Idee extends Expr
final case class Bag(elements: Top*) extends Pot
final case class APair(first: Expr, second: Expr) extends Expr
final case class Signed(contents: Expr, signatory: Expr) extends Expr

final case class Merge(one: Expr, two: Expr) extends Expr
final case class Contains(bag: Expr, element: Expr) extends Expr
final case class Equals(one: Expr, two: Expr) extends Expr
final case class Branch(one: Expr, two: Expr) extends Expr
final case class Copy(outs: Expr) extends Expr
final case class Public(key: Expr) extends Expr

final case class Broadcast(key: Expr) extends Expr

final case class Start(key: Expr) extends Expr
final case class Pause(key: Expr) extends Expr
final case class Dissolve(key: Expr) extends Expr
final case class Kill(key: Expr) extends Expr
final case class Move(key: Expr) extends Expr

final case class Code(code: Top) extends Expr
final case class AsCode(value: Expr) extends Expr
final case class CodeZip(expr: Expr) extends Expr
final case class Escape(expr: Expr) extends Pot
