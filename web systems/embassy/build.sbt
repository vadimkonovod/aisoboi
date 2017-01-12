name := "embassy"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.8"

libraryDependencies += javaJpa

libraryDependencies += "dom4j" % "dom4j" % "1.6"

libraryDependencies += "org.mockito" % "mockito-core" % "2.1.0"

libraryDependencies += javaWs % "test"

libraryDependencies ++= Seq(
  cache
)

libraryDependencies += "org.mongodb" % "mongodb-driver-async" % "3.2.2"
