package ante

trait Abstract {

  sealed trait Content

  final class Name extends Content
  sealed trait Key extends Content {
    def name: Name
  }


  sealed trait Message
  final case class Plain(content: Content) extends Message
  final class Signed(val content: Content, key: Key) extends Message {
    val name = key.name
  }

  
}
