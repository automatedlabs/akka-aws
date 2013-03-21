import sbt._
import sbt.Keys._

object AkkaAwsBuild extends Build {

  lazy val akkaAws = Project(
    id = "akka-aws",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "akka-aws",
      organization := "com.automatedlabs",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.0",
      libraryDependencies ++= Seq( 
        "com.typesafe.akka" %% "akka-actor" % "2.1.1",
        "com.typesafe.akka" %% "akka-testkit" % "2.1.1",
        "com.amazonaws" % "aws-java-sdk" % "1.3.33",
        "org.scalatest" %% "scalatest" % "1.9.1" % "test",
        "org.scalamock" %% "scalamock-scalatest-support" % "3.0.1" % "test"
      ),
      scalacOptions ++= Seq("-feature", "-deprecation"),
      testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oDF"),
      parallelExecution in Test := false
    )
  )
}
