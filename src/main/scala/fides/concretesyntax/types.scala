package fides.concretesyntax

import fides.concretesyntax.Precedences.Precedence
import fides.syntax.code.ValType
import fides.syntax.connectors.Match
import util.TList

import scala.annotation.targetName
import scala.compiletime.{asMatchable, constValue, erasedValue, error, summonInline}
import scala.deriving.Mirror
import scala.deriving.Mirror.ProductOf
import scala.util.matching.Regex

/**
  * @tparam T a tuple type whose element types are singleton types
  * @return a [[TList]] of the values of the singleton types
  */
inline def tupleTypesAsValues[T <: Tuple]: TList[Any] = inline erasedValue[T] match
  case _: EmptyTuple     => TList.Empty
  case _: (head *: tail) => constValue[head] :: tupleTypesAsValues[tail]

/**
  * Tries to guess the correct polarity character based on the name of the primitive.
  */
private def polarityFromName(name: String): Char =
  val xctr: Regex = """Un[A-Z][a-z]+""".r
  val ptrn: Regex = """Match[A-Z][a-z]+""".r
  name match
    case xctr() => '-'
    case ptrn() => '~'
    case _      => '+'

/**
  * @return a single-line string representation of [[term]]
  */
inline def singleLineString[T](term: T)(using Precedence): String =
  val s = summonInline[ShowableTerm[T]]
  val string = inline term.asMatchable match
    case caseClass: Product =>
      val args = argSingleLineStrings(caseClass)(using s.innerPrecedence)
      assert(s.keys.length == args.length)
      val keyedArgs = (for (key, arg) <- s.keys.zip(args) yield key + " " + arg).mkString(", ")
      s.header + keyedArgs + s.tailer
    case _ => error(s"singleLineString is not supported for $term as of yet.")
  if summon[Precedence] < s.outerPrecedence then string else
    s"(${s.polarity} $string ${s.polarity})"

/**
  * @return a [[TList]] of single-line string representations of the elements of [[product]]
  */
inline def argSingleLineStrings[P <: Product](product: P)(using Precedence): TList[String] =
  inline Tuple.fromProductTyped(product)(using summonInline[ProductOf[P]]) match
    case _: EmptyTuple => TList.Empty
    case head *: tail  => singleLineString(head) :: argSingleLineStrings(tail)

/**
  * Various strings that can be used when generating a string representation for a term of type [[T]],
  * leaving the formatting open.
  */
trait ShowableTerm[T]:
  def innerPrecedence: Precedence
  def outerPrecedence: Precedence
  def polarity: Char
  def header: String
  def tailer: String
  val keys: TList[String]
end ShowableTerm

object ShowableTerm:
  /**
    * Provides automatic derivation of [[ShowableTerm]] for product types.
    */
  inline def derived[T](using mirror: Mirror.Of[T]): ShowableTerm[T] =
    inline mirror match
      case p: Mirror.ProductOf[T] => new ShowableTerm[T]:
        override def outerPrecedence: Precedence = Precedences.parentheses
        override def innerPrecedence: Precedence = Precedences.comma
        override def polarity: Char = polarityFromName(constValue[mirror.MirroredLabel])
        override def header: String = constValue[mirror.MirroredLabel].toLowerCase.nn + "("
        override def tailer: String = ")"
        override val keys: TList[String] = tupleTypesAsValues[mirror.MirroredElemLabels].map(_.toString)
      case _ => error("ShowableTerm derivation is currently only supported for product types.")
end ShowableTerm

given [T <: ValType]: ShowableTerm[Match[T]] with
  def outerPrecedence: Precedence = Precedences.space
  def innerPrecedence: Precedence = Precedences.space
  def polarity: Char = '-'
  def header: String = "match"
  def tailer: String = ""
  val keys = "against" :: "default to" :: TList.Empty

/**
  * Precedence is used to keep track of when additional parentheses are necessary.
  */
object Precedences:
  opaque type Precedence = Double
  val comma: Precedence = 0
  val space: Precedence = 1
  val parentheses: Precedence = Double.PositiveInfinity
  extension (self: Precedence)
    @targetName("smallerOrEqual") inline def <=(inline other: Precedence): Boolean = self <= other
    @targetName("smaller"       ) inline def < (inline other: Precedence): Boolean = self <  other
    @targetName("biggerOrEqual" ) inline def >=(inline other: Precedence): Boolean = self >= other
    @targetName("bigger"        ) inline def > (inline other: Precedence): Boolean = self >  other
end Precedences
