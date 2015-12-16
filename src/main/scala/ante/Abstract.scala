package ante

trait Abstract {

  sealed trait Content

  sealed trait Key extends Content {
    def name: Name
  }

  sealed trait Message

  final class Name extends Content

  final case class Plain(content: Content) extends Message
  final class Signed(val content: Content, key: Key) extends Message {
    val name = key.name
  }
}
