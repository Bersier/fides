package scides

//import scala.concurrent.{ExecutionContext, Future}
import observable.Async

import scala.quoted.{Expr, Quotes, staging}

//import ExecutionContext.Implicits.global

given staging.Compiler =
  object Dummy
  staging.Compiler.make(Dummy.getClass.getClassLoader.nn)

def test(): Async =
  println("Java version: " + System.getProperty("java.version"))
  println("Scala version: " + dotty.tools.dotc.config.Properties.simpleVersionString)
  val sphere = new Scidesphere
  val certificateReceiver = sphere.Channel[sphere.LaunchCertificate](certificate => Async(println(certificate)))
  // todo add externally mutable behavior to the example
  sphere.Launcher.send(
    (
      '{ println("Hello") }, // todo be able to print code
      certificateReceiver.identifier,
    )
  )

type Consumer[T] = T => Async
val NoOp: Consumer[Any] = m => Async()

final class Scidesphere:
  sealed trait Identifier derives CanEqual

  sealed trait IdentifierKey:
    val identifier: Identifier
  end IdentifierKey

  final case class Signed[+T, +S <: Identifier] private(message: T, signature: S)
  object Signed:
    def apply[T, S <: IdentifierKey](message: T, key: S): Signed[T, key.identifier.type] =
      new Signed(message, key.identifier)
  end Signed

  sealed trait Channel[-T] extends Identifier:
    def send(message: T): Async
  object Channel:
    def apply[T](behavior: Consumer[T]): ChannelKey[T] = UserChannel(behavior)
  end Channel

  sealed trait ChannelKey[T] extends IdentifierKey:
    val identifier: Channel[T]
    // todo add register and deregister back?
  end ChannelKey

  private[Scidesphere] final class UserChannel[T](behavior: Consumer[T]) extends Channel[T]:
    def send(message: T): Async = behavior(message) // todo add Future( )
  private[Scidesphere] object UserChannel:
    def apply[T](behavior: Consumer[T]): ChannelKey[T] = new ChannelKey[T]:
      val identifier: Channel[T] = new UserChannel(behavior)
  end UserChannel

  object Launcher extends Channel[LauncherInput]:
    def send(message: LauncherInput): Async = // todo add (Future )
      val (_, certificateRecipient) = message
      staging.run(message._1)
      certificateRecipient.send(Signed(staging.withQuotes(message._1), Key))

    private object Key extends Scidesphere.this.ChannelKey[LauncherInput]:
      val identifier: Launcher.type = Launcher.this
    end Key
  end Launcher

  type LaunchCertificate = Signed[Expr[Unit], Launcher.type]
  type LauncherInput = (Quotes ?=> Expr[Unit], Channel[LaunchCertificate])
end Scidesphere
