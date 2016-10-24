name          := "htwg-set-game-scala"
organization  := "de.htwg.se"
version       := "0.0.1"
scalaVersion  := "2.11.8"
scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-encoding", "utf8")

resolvers += Resolver.jcenterRepo

libraryDependencies ++= {
  Seq(
    "org.scalatest" %% "scalatest"                   % "3.0.0"                % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % "latest.release"       % "test"
  )
}
