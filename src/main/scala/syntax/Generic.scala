package syntax

final class Generic(body : Seq[Taker[Nothing]] => Seq[Taker[Nothing]]) {

  final case class Instance(out: Seq[Taker[Nothing]]) {
    private[this] val in = body(out)

    final case class In(i :Int) extends Taker[Val] {
      require(0 <= i && i < in.size)

      override def apply(v: Val): Unit = {
        in(i).asInstanceOf[Taker[Val]](v)
      }
    }
  }
}
