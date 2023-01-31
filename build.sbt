lazy val root = project
  .in(file("."))
  .settings(
    name := "Fides",
    scalaVersion := "3.2.2",
    libraryDependencies += "org.scala-lang" %% "scala3-staging" % scalaVersion.value,
    libraryDependencies += "dev.zio" %% "zio" % "2.0.6",
    libraryDependencies += "dev.zio" %% "zio-streams" % "2.0.6",
    javacOptions ++= Seq("--release", "19", "--enable-preview"),
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
