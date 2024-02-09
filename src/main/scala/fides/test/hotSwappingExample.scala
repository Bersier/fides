package fides.test

import fides.syntax.code.*
import fides.syntax.connectors.*
import fides.syntax.control.*
import fides.syntax.identifiers.*
import fides.syntax.meta.*
import fides.syntax.processes.*
import fides.syntax.values.*
import izumi.reflect.Tag
  
def randomBitService(channel: InpChan[OutChan[Bool]])(using Context): Code[Process] =
  Repeated(
    Send(
      contents = RandomBit(),
      recipient = Inp(channel)
    )
  )

def launchExample(using Context): Code[?] =
  val serviceChannel = InpChan[OutChan[Bool]]()
  Concurrent(Args(
    Forward(
      Launch(code = Quote(randomBitService(serviceChannel))), 
      Ignore()
    )
  ))

def ifThenElse(testee: Code[Expr[Bool]], trueBranch: Code[Process], falseBranch: Code[Process])
  (using Context): Code[?] =
  import scala.language.implicitConversions
  val trueSignal = Channel[Pulse]()
  val falseSignal = Channel[Pulse]()
  Concurrent(Args(
    Switch(
      input = Pulse,
      testee = testee,
      cases = Args(
        Case(
          testValue = True,
          extractor = Out(trueSignal)
        ),
        Case(
          testValue = False,
          extractor = Out(falseSignal)
        )
      )
    ),
    OnHold(startSignal = Inp(trueSignal), body = trueBranch),
    OnHold(startSignal = Inp(falseSignal), body = falseBranch),
  ))
  
def dynamicCollection[T <: ValType : Tag](put: InpChan[T], pop: InpChan[OutChan[T]])(using Context): Code[Process] =
  import scala.language.implicitConversions
  val element = Channel[T]()
  val signal = Channel[T]()
  Repeated(
    Scope(
      localIdentifiers = Args(element, signal),
      Concurrent(Args(
        Spread(
          Inp(put),
          Args(Out(element), Out(signal))
        ),
        OnHold(
          startSignal = Signal(Inp(signal)),
          body = Send(Inp(element), Inp(pop))
        ),
      ))
    )
  )


/**
  * When a new service is launched, the input controller will be notified to start the hot-swapping.
  * All the new queries will be sent to the new service's address.
  * However, we must make sure the old service is done with the already sent queries in order to kill it.
  * The input controller will keep track of the requests sent out to the different services.
  * The ones that are done will be deleted.
  * Only when the list of addresses sent to the old service becomes empty, it will be killed.
  */
//def inputController(using Context): Code[?] =
//  val controllerChannel = Channel[Channel[Bool]]()
//  val oldServiceChannel = Channel[Channel[Bool]]()
//  val newServicechannel = Channel[Channel[Bool]]()
  

  



 
