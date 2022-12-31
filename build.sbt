lazy val root = project
  .in(file("."))
  .settings(
    name := "Fides",
    scalaVersion := "3.2.1",
    libraryDependencies += "org.scala-lang" %% "scala3-staging" % scalaVersion.value,
    javacOptions ++= Seq("--release", "19", "--enable-preview"),
    javaOptions += "--enable-preview",
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
