package fides.syntax

sealed trait TopM private[syntax]()
final abstract class BotM extends TopM
