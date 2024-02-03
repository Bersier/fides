package fides.test

import fides.syntax.code.Code
import fides.syntax.connectors.{Forward, Ignore}
import fides.syntax.identifiers.{Cell, CompareAndSwap, Context, Read, Write}
import fides.syntax.values.{Add, Integer, Pulse}

def writeToCellExample(using Context): Code[?] =
  val myCell = Cell(Integer(0), name = "myCell")
  Forward(
    Add(Read(Pulse, myCell), Integer(10)),
    Write(signal = Ignore(), iD = myCell),
  )

def compareAndSwapExample(using Context): Code[?] =
  val myCell = Cell(Integer(0), name = "myCell")
  CompareAndSwap(
    testValue = Integer(0),
    newValue = Integer(13),
    iD = myCell
  )
