package fides.test

import fides.syntax.code.*
import fides.syntax.connectors.*
import fides.syntax.control.*
import fides.syntax.identifiers.*
import fides.syntax.meta.*
import fides.syntax.processes.*
import fides.syntax.values.*
import izumi.reflect.Tag


def dynamicCollection[T <: ValType : Tag](put: InpChan[T], pop: InpChan[OutChan[T]])
  (using Context): Code[Process] =
  import scala.language.implicitConversions
  val element = Channel[T]()
  val release = Channel[T]()
  Repeated(
    Scope(
      localIdentifiers = Args(element, release),
      Concurrent(Args(
        Spread(
          Inp(put),
          Args(Out(element),  Out(release))
        ),
        OnHold(
          Signal(Inp(release)),
          Send(Inp(element), Inp(pop))
        ),
      ))
    )
  )

def launchExample(using Context): Code[?] =
  val serviceChannel = Channel[Bool]()
  Concurrent(Args(
    Forward(
      Launch(code = Quote(randomBitService(serviceChannel))),
      Ignore()
    )
  ))

def randomBitService(response: Channel[Bool])(using Context): Code[Process] =
  import scala.language.implicitConversions
  Repeated(
    Forward(
      RandomBit(),
      Out(response)
    )
  )

//def hotSwappingRandomBitServiceExample(hotswappingSignal: InpChan[Pulse], request: InpChan[OutChan[Bool]])
//  (using Context): Code[?] =
//  import scala.language.implicitConversions
//  val startSignal = Channel[Order]()
//  val killSignal = Channel[Order]()
//  val response = Channel[Bool]()
//  Concurrent(Args(
//    OnHold(
//      Inp(hotswappingSignal),
//      Concurrent(Args(
//        Forward(Kill, Out(killSignal)),
//        Forward(Start, Out(startSignal)),
//      ))
//    ),
//    dynamicCollection(response, request),
//    Awake(
//      killSignal,
//      randomBitService(response)
//    ),
//    Asleep(
//      startSignal,
//      randomBitService(response)
//    ),
//  ))