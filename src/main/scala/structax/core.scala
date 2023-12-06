package structax

// Maybe we should have a separate representation for execution, and have the most natural and agnostic representation
// for, well, representation? Maybe the execution representation could be concrete types with additional features for
// efficient execution?
//trait Proc[ProcType <: Proc[ProcType]] {
//  this: ProcType =>
//  def copy: ProcType
//}

class Channel(symbol: Symbol) extends AnyVal:
  def input: Input = Input(symbol)
  def ouput: Ouput = Ouput(symbol)
end Channel

class Input(symbol: Symbol) extends AnyVal:
  def channel: Channel = Channel(symbol)
end Input

class Ouput(symbol: Symbol) extends AnyVal:
  def channel: Channel = Channel(symbol)
end Ouput

type Opposite[Xput] = Xput match
  case Input => Ouput
  case Ouput => Input
  case Opposite[xput] => xput

sealed trait Proc
sealed trait Val

trait Message extends Proc:
  def value: Val
  def target: Ouput
end Message

trait Send extends Proc:
  def message: Input
  def target: Input
end Send

trait Launch extends Proc:
  def code: Input
  def certificate: Ouput
end Launch

trait Catch extends Proc:
  def handle: Input
  def code: Ouput
end Catch
