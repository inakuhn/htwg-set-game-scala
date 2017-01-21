name := "htwg-set-game-scala"
organization := "de.htwg.se"
version := "0.0.1"
scalaVersion := "2.11.8"
scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-encoding", "utf8")

resolvers += Resolver.jcenterRepo
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshot"
libraryDependencies ++= {
  Seq(
    "org.scalatest" %% "scalatest" % "3.0.0" % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.3.0" % "test",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
    "ch.qos.logback" % "logback-classic" % "1.1.7",
    "uk.org.lidalia" % "slf4j-test" % "1.2.0" % "test",
    "com.typesafe.akka" % "akka-remote_2.11" % "2.4.12",
    "com.typesafe.akka" % "akka-slf4j_2.11" % "2.4.12",
    "com.typesafe.akka" %% "akka-testkit" % "2.4.12" % "test"
  )
}
