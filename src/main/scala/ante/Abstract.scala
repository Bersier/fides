package ante

trait Abstract {

  sealed trait Content

  sealed trait Port extends Content {
    def name: Name
  }

  sealed trait Message

  final class Name extends Content

  final case class Plain(content: Content) extends Message
  final class Signed(val content: Content, key: Port) extends Message {
    val name = key.name
  }
}
