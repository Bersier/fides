package fides.syntax

/**
  * Representation of names
  */
opaque type TopK = String
private[syntax] final val launcherK: TopK = "Launcher"
type LauncherK = launcherK.type
type BotK = Nothing

final abstract class GenV[+`D+` >: BotD <: OffTopD, -`D-` >: OffBotD <: TopD]
type TopV = GenV[OffTopD, OffBotD]
type `V+`[+D >: BotD <: TopD] = GenV[D, OffBotD]
type `V-`[-D >: BotD <: TopD] = GenV[OffTopD, D]
type `V0`[D >: BotD <: TopD] = GenV[D, D]
type BotV = GenV[BotD, TopD]
