name          := "htwg-set-game-scala"
organization  := "de.htwg.se"
version       := "0.0.1"
scalaVersion  := "2.11.8"
scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-encoding", "utf8")

resolvers += Resolver.jcenterRepo

libraryDependencies ++= {
  Seq(
    "org.scalatest" %% "scalatest"                   % "3.0.0"                % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.3.0"       % "test",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
    "org.slf4j" % "slf4j-log4j12" % "1.7.21",
    "org.scala-lang" % "scala-swing" % "2.11.0-M7"
  )
}
