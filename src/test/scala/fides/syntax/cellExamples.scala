package fides.syntax

import fides.syntax.types.Code
import fides.syntax.connectors.{Forward, Ignore}
import fides.syntax.identifiers.naming.Context
import fides.syntax.identifiers.{Cell, CompareAndSwap, Read, Write}
import fides.syntax.meta.Args
import fides.syntax.values.{Add, NaturalNumber, Pulse}

//def writeToCellExample(using Context): Code[?] =
//  val myCell = Cell(NaturalNumber(0), name = "myCell")
//  Forward(
//    Add(Args(Read(Pulse, myCell), NaturalNumber(10))),
//    Write(signal = Ignore(), iD = myCell),
//  )
//
//def compareAndSwapExample(using Context): Code[?] =
//  val myCell = Cell(NaturalNumber(0), name = "myCell")
//  CompareAndSwap(
//    testValue = NaturalNumber(0),
//    newValue = NaturalNumber(13),
//    iD = myCell
//  )
