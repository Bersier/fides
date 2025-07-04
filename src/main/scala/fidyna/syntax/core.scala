package fidyna.syntax

sealed trait Code private[syntax]()
object Code

final case class Args private[syntax](args: Code*)

transparent sealed trait Sentence private[syntax]() extends Code
object Sentence:
  object Empty extends Sentence
  final case class Part private[syntax](head: Code, tail: Sentence) extends Sentence
end Sentence

transparent sealed trait Word private[syntax]() extends Code

transparent sealed trait Value private[syntax]() extends Word

transparent sealed trait Header private[syntax]() extends Word

/*
Use extractor objects?
https://docs.scala-lang.org/tour/extractor-objects.html
https://docs.scala-lang.org/scala3/reference/changed-features/pattern-matching.html
*/

object Pulse extends Value
object Pair extends Header

// Declarations
// Names
// Scopes
