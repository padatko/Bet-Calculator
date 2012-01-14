name := "Bet-Calculator"

version := "1.0"

scalaVersion := "2.9.1"

seq(webSettings: _*)

resolvers ++= Seq(
	"Java.net Maven2 Repository" at "http://download.java.net/maven/2/"
)

libraryDependencies ++= Seq(
	"org.specs2" %% "specs2" % "1.6.1",
	"org.specs2" %% "specs2-scalaz-core" % "6.0.1" % "test",
	"org.scalatest" %% "scalatest" % "latest.release" % "test",
	"org.scala-lang" % "scala-swing" % "2.9.1",
  	"net.liftweb" %% "lift-webkit" % "latest.release",
  	"org.eclipse.jetty" % "jetty-webapp" % "latest.release" % "container, test", 
  	"ch.qos.logback" % "logback-classic" % "latest.release"
)

scalacOptions ++= Seq(
	"-unchecked",
	"-deprecation"
)

testOptions in Test ++= Seq(
	Tests.Argument(TestFrameworks.ScalaTest, "-oS")
)