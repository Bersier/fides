
lazy val root = (project in file("."))
  .dependsOn(
    RootProject(uri("https://github.com/Bersier/observable.git")),
    RootProject(uri("https://github.com/Bersier/TypeLevelNumbers.git")),
  ) // to reload the dependencies, delete all copies of it in ~/.sbt/1.0/staging
  .settings(
    name := "Fides",
    version := "0.1.0",

    // See https://www.scala-lang.org/news/new-scala-nightlies-repo.html
    scalaVersion := "3.8.1-RC1-bin-20251123-91351e3-NIGHTLY",
    resolvers += Resolver.scalaNightlyRepository,

    libraryDependencies ++= Seq(
      "org.scala-lang" %% "scala3-staging" % scalaVersion.value,
      // See https://docs.scala-lang.org/api/all.html
      scalaOrganization.value % "scala-library" % "2.13.17",
      "dev.zio" %% "zio" % "2.1.14",
//      "dev.zio" %% "zio-streams" % "2.1.14",
//      "org.typelevel" %% "cats-core" % "2.12.0",
//      "org.typelevel" %% "kittens" % "3.4.0",
//      "org.typelevel" %% "shapeless3-deriving" % "3.4.3",
    ),

    // See https://github.com/lampepfl/dotty/blob/main/compiler/src/dotty/tools/dotc/config/ScalaSettings.scala
    scalacOptions ++= Seq(
//      "-Vprofile",
//      "-Werror",
      "-Wenum-comment-discard",
      "-Wimplausible-patterns",
      "-Wnonunit-statement",
      "-Wrecurse-with-default",
      "-Wsafe-init",
      "-Wtostring-interpolated",
      "-WunstableInlineAccessors",
      "-Wunused:all",
      "-Wvalue-discard",
      "-Wwrong-arrow",
      "-Xcook-docs",
      "-Xdebug-macros",
//      "-Xdisable-assertions",
      "-Xkind-projector:underscores",
//      "-Xmax-inlines", "128",
      "-Ycheck-all-patmat",
//      "-Ycheck-reentrant",
      "-Ydebug-pos",
      "-Yexplicit-nulls",
//      "-Yrequire-targetName",
//      "-Ysafe-init-global", // This can cause extreme compilation slowdowns
      "-deprecation",
      "-experimental",
      "-explain",
      "-feature",
//      "-language:experimental.captureChecking",
//      "-language:experimental.genericNumberLiterals",
//      "-language:experimental.into",
//      "-language:experimental.modularity",
//      "-language:experimental.namedTypeArguments",
//      "-language:experimental.pureFunctions",
      "-language:strictEquality",
      "-new-syntax",
      "-release:21",
      "-source:future",
      "-unchecked",
    ),
)
