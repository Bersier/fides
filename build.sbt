name := "Fides"
version := "0.1.0"

// See https://repo1.maven.org/maven2/org/scala-lang/scala3-compiler_3/
scalaVersion := "3.6.0-RC1-bin-20240823-5101daf-NIGHTLY"

libraryDependencies ++= Seq(
  "org.scala-lang" %% "scala3-staging" % scalaVersion.value,
  // See https://docs.scala-lang.org/api/all.html
  scalaOrganization.value % "scala-library" % "2.13.14",
  "dev.zio" %% "zio" % "2.1.7",
//  "dev.zio" %% "zio-streams" % "2.1.7",
//  "org.typelevel" %% "cats-core" % "2.12.0",
//  "org.typelevel" %% "kittens" % "3.3.0",
//  "org.typelevel" %% "shapeless3-deriving" % "3.4.1",
)

// See https://github.com/lampepfl/dotty/blob/main/compiler/src/dotty/tools/dotc/config/ScalaSettings.scala
scalacOptions ++= Seq(
//  "-Vprofile",
//  "-Werror",
  "-Wimplausible-patterns",
  "-Wnonunit-statement",
  "-Wsafe-init",
  "-Wunused:all",
  "-Xcook-docs",
  "-Xdebug-macros",
//  "-Xdisable-assertions",
  "-Xkind-projector:underscores",
//  "-Xmax-inlines", "128",
  "-Ycheck-all-patmat",
//  "-Ycheck-reentrant",
  "-Ydebug-pos",
  "-Yexplicit-nulls",
  "-Yrequire-targetName",
  "-Ysafe-init-global",
  "-deprecation",
  "-experimental",
  "-explain",
  "-feature",
//  "-language:experimental.captureChecking",
//  "-language:experimental.genericNumberLiterals",
//  "-language:experimental.into",
//  "-language:experimental.modularity",
//  "-language:experimental.namedTypeArguments",
//  "-language:experimental.pureFunctions",
  "-language:strictEquality",
  "-new-syntax",
  "-release:21",
  "-source:future",
  "-unchecked",
)
