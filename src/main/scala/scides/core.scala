package scides

import util.Async
import scala.concurrent.{Future, ExecutionContext}
import scala.quoted.{Expr, Quotes, staging}
import ExecutionContext.Implicits.global

given staging.Compiler = staging.Compiler.make(getClass.getClassLoader.nn)

@main def test(): Unit =
  println("Java version: " + System.getProperty("java.version"))
  val sphere = new Scidesphere
  val certificateReceiver = sphere.Channel.newKey[sphere.LaunchCertificate]
  certificateReceiver.register(certificate => Async(println(certificate)))
  sphere.Launcher.send(
    (
      '{ println("Hello") }, // todo be able to print code
      certificateReceiver.channel,
    )
  )
  Thread.sleep(100)

type Consumer[T] = T => Async
val NoOp: Consumer[Any] = m => Async()

final class Scidesphere:
  sealed trait Channel[-T]: // todo comparable? (also with keys?)
    def send(message: T): Async
  object Channel:
    def apply[T](): Channel[T] = new UserChannel
    def newKey[T]: Key[T] = UserChannel.newKey
  end Channel

  sealed trait Key[T]:
    val channel: Channel[T]
    def register(recipient: Consumer[T]): Unit
    def deregister(): Unit // todo redundant?
  end Key

  private[Scidesphere] final class UserChannel[T] extends Channel[T]:
    @volatile protected[this] var registered: Consumer[T] = NoOp

    def send(message: T): Async = Async(registered(message)) // todo add Future( )

    private[this] object Key extends Scidesphere.this.Key[T]:
      val channel: Channel[T] = UserChannel.this

      def register(recipient: Consumer[T]): Unit =
        registered = recipient

      def deregister(): Unit =
        registered = NoOp
    end Key
  private[Scidesphere] object UserChannel:
    def newKey[T]: Key[T] = (new UserChannel).Key
  end UserChannel

  final case class Signed[T, +S <: PublicKey] private(message: T, signature: S)
  object Signed:
    def apply[T](message: T, key: PrivateKey): Signed[T, key.channel.type] = Signed(message, key.channel)
  end Signed
  type PrivateKey = Key[?]
  type PublicKey = Channel[?]

  object Launcher extends Channel[LauncherInput]:
    def send(message: LauncherInput): Async = Async { // todo add (Future )
      val (_, certificateRecipient) = message
      certificateRecipient.send(Signed(staging.withQuotes(message._1), Key))
      staging.run(message._1)
    }

    private[this] object Key extends Scidesphere.this.Key[LauncherInput]:
      val channel: Launcher.type = Launcher.this
      def register(recipient: Consumer[LauncherInput]): Unit = throw new AssertionError()
      def deregister(): Unit = throw new AssertionError()
    end Key
  end Launcher
  type LaunchCertificate = Signed[Expr[Unit], Launcher.type]
  type LauncherInput = (Quotes ?=> Expr[Unit], Channel[LaunchCertificate])
end Scidesphere
