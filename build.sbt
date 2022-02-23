lazy val root = project
  .in(file("."))
  .settings(
    name := "Fides",
    scalaVersion := "3.1.1",
    libraryDependencies += "org.scala-lang" %% "scala3-staging" % scalaVersion.value,
    scalacOptions ++= Seq(
      "-Ycheck-all-patmat",
      "-Ycheck-reentrant",
      "-Ycook-comments",
      "-Ydebug-pos",
      "-Yexplicit-nulls",
      "-Ysafe-init",
      "-explain",
      "-language:strictEquality",
    ),
    version := "0.1.0",
  )
