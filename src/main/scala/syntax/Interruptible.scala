package syntax

class Interruptible(val interruptee: Taker[Unit]) {
  val run = new Idee // todo these need to 'send back a value when they are done'
  val kill = new Idee
  val pause = new Idee
}
