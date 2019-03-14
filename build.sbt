name := "cengine"

version := "1.0"

scalaVersion := "2.11.7"

resolvers += "dnvriend at bintray" at "http://dl.bintray.com/dnvriend/maven"
resolvers += "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"
resolvers +=  "sonatype releases"  at "http://oss.sonatype.org/content/repositories/releases"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.0-M11" % "test",
  "org.javamoney" % "moneta" % "1.0",
  "com.github.nscala-time" %% "nscala-time" % "2.4.0",
  "org.slf4j" % "slf4j-api" % "1.7.2",
  "ch.qos.logback" % "logback-classic" % "1.0.7",
  "com.typesafe.akka" %% "akka-actor" % "2.4.0",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.0",
  "com.typesafe.akka" %% "akka-persistence" % "2.4.0",
  "com.typesafe.akka" %% "akka-remote" % "2.4.0",
  "com.github.dnvriend" %% "akka-persistence-jdbc" % "1.2.2",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  "org.scalikejdbc" %% "scalikejdbc-async" % "0.5.+",
  "com.github.mauricio" %% "postgresql-async" % "0.2.+",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)

mainClass in (Compile, run) := Some("se.tain.Launcher")