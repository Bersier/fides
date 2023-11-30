lazy val root = project
  .in(file("."))
  .settings(
    name := "Fides",
    scalaVersion := "3.3.1",
    libraryDependencies += "org.scala-lang" %% "scala3-staging" % scalaVersion.value,
    libraryDependencies += "dev.zio" %% "zio" % "2.0.19",
    libraryDependencies += "dev.zio" %% "zio-streams" % "2.0.19",
    javacOptions ++= Seq("--release", "20", "--enable-preview"),
    javaOptions += "--enable-preview",
    scalacOptions ++= Seq(
      // "-Xdisable-assertions",
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
