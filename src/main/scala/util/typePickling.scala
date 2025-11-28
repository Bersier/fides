package util

import scala.compiletime.ops.any.ToString
import scala.compiletime.ops.int.{+, -, Negate, `*`}
import scala.compiletime.ops.string.{Length, Matches, Substring, + as ++}

type Encoded[T] <: String = T match
  case EmptyTuple => "EmptyTuple"
  case h *: t => "Tuple(" ++ Encoded[h] ++ ", " ++ Encoded[t] ++ ")"
  case String => "String(" ++ ToString[T] ++ ")"
  case _ => ToString[T]

type Decoded[S <: String] = Matches[S, "EmptyTuple"] match
  case true => EmptyTuple
  case false => Matches[S, "Tuple(.+, .+)"] match
    case true => Parsed[Substring[S, 6, Length[S] - 1], 0, ""] match
      case (h, t) => Decoded[h] *: Decoded[t]
    case false => Matches[S, "String(.+)"] match
      case true => Substring[S, 7,  Length[S] - 1]
      case false => Matches[S, "(0)|(-?[1-9][0-9]*)"] match
        case true => DecodedInt[S]
        case false => Any

private type DecodedInt[S <: String] <: Int = Matches[S, "0"] match
  case true => 0
  case false => Matches[S, "-[1-9][0-9]*"] match
    case true => Negate[DecodedIntPart[Substring[S, 1, Length[S]]]]
    case false => DecodedIntPart[S]

private type DecodedIntPart[S <: String] <: Int = Matches[S, ""] match
  case true => 0
  case false => FirstDigit[S] * TenPow[Length[S] - 1] + DecodedIntPart[Substring[S, 1, Length[S]]]

/**
  * Reduces to 10^[[I]] - 1^.
  */
private type TenPow[I <: Int] <: Int = I match
  case 0 => 1
  case _ => 10 * TenPow[I - 1]

private type FirstDigit[S <: String] <: Int = Matches[S, "1[0-9]*"] match
  case true => 1
  case false => Matches[S, "2[0-9]*"] match
    case true => 2
    case false => Matches[S, "3[0-9]*"] match
      case true => 3
      case false => Matches[S, "4[0-9]*"] match
        case true => 4
        case false => Matches[S, "5[0-9]*"] match
          case true => 5
          case false => Matches[S, "6[0-9]*"] match
            case true => 6
            case false => Matches[S, "7[0-9]*"] match
              case true => 7
              case false => Matches[S, "8[0-9]*"] match
                case true => 8
                case false => Matches[S, "9[0-9]*"] match
                  case true => 9

// todo doesn't take escape characters into account
/**
  * Parses a comma-separated list of arguments.
  *
  * @tparam S to be parsed
  * @tparam I depth
  * @tparam A accumulator
  */
private type Parsed[S <: String, I <: Int, A <: String] <: (String, String) = Matches[S, "\\).*"] match
  case true => I match
    case 1 => (A ++ ")", Substring[S, 3, Length[S]])
    case _ => Parsed[Substring[S, 1, Length[S]], I - 1, A ++ ")"]
  case false => Matches[S, "\\(.*"] match
    case true => Parsed[Substring[S, 1, Length[S]], I + 1, A ++ "("]
    case false => I match
      case 0 => Matches[S, ", .*"] match
        case true => (A, Substring[S, 2, Length[S]])
        case false => Parsed[Substring[S, 1, Length[S]], I, A ++ Substring[S, 0, 1]]
      case _ => Parsed[Substring[S, 1, Length[S]], I, A ++ Substring[S, 0, 1]]

private def picklingExamples(): Unit =
  summon[Encoded[(0, "Hel\"lo")] =:= "Tuple(0, Tuple(String(Hel\"lo), EmptyTuple))"]
  summon[Decoded["String(Hello)"] =:= "Hello"]

   summon[Decoded["Tuple(0, Tuple(String(Hel\"lo), EmptyTuple))"] =:= (0, "Hel\"lo")]

   summon[Decoded["Tuple(0, EmptyTuple)"] =:= Tuple1[0]]

//   Decoded["Tuple(0, EmptyTuple)"]
//   Parsed["0, EmptyTuple", 0, ""] match case (h, t) => Decoded[h] *: Decoded[t]
//   Parsed[", EmptyTuple", 0, "0"] match case (h, t) => Decoded[h] *: Decoded[t]
//   ("0", "EmptyTuple") match case (h, t) => Decoded[h] *: Decoded[t]
//   Decoded["0"] *: Decoded["EmptyTuple"]
//   0 *: EmptyTuple
