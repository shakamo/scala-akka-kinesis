name := "akka-quickstart-scala"

version := "1.0"

scalaVersion := "2.12.10"

lazy val akkaVersion = "2.6.1"

resolvers in ThisBuild += Resolver.bintrayRepo("streetcontxt", "maven")

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.1.0" % Test,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.github.seratch" %% "awscala" % "0.8.+",
  "com.amazonaws" % "amazon-kinesis-client" % "1.13.0",
  "software.amazon.kinesis" % "amazon-kinesis-client" % "2.2.7",
  "com.lightbend.akka" %% "akka-stream-alpakka-kinesis" % "0.17",
  "com.streetcontxt" %% "kcl-akka-stream" % "2.0.3"
)
