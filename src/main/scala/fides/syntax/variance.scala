package fides.syntax

final abstract class `D:`[+`D+` >: BotD <: OffTopD, -`D-` >: OffBotD <: TopD]
type `TopD:` = `D:`[OffTopD, OffBotD]
type `D+`[+D >: BotD <: TopD] = `D:`[D, OffBotD]
type `D-`[-D >: BotD <: TopD] = `D:`[OffTopD, D]
type `D0`[D >: BotD <: TopD] = `D:`[D, D]
type `BotD:` = `D:`[BotD, TopD]

final abstract class `K:`[+`K+` >: BotK <: TopK, -`K-` >: BotK <: TopK]
type `TopK:` = `K:`[TopK, BotK]
type `K+`[+K >: BotK <: TopK] = `K:`[K, BotK]
type `K-`[-K >: BotK <: TopK] = `K:`[TopK, K]
type `K0`[K >: BotK <: TopK] = `K:`[K, K]
type `BotK:` = `K:`[BotK, TopK]
