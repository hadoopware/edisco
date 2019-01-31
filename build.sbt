parallelExecution in Test := false

initialize ~= { _ =>
  System.setProperty("config.file", "conf/application.conf")
}

val akkaHttpV = "10.1.3"
val scalaTestV = "3.0.5"
val slickVersion = "3.2.3"
val circeV = "0.9.3"
val sttpV = "1.1.13"

val shared = Seq(
  retrieveManaged := true,
  version := "0.0.1-SNAPSHOT",
  organization := "io.syspulse",
  scalaVersion := "2.12.0",
  crossScalaVersions := Seq("2.10.0", "2.11.0"),
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-optimize", "-feature", "-language:existentials", "-language:implicitConversions", "-language:higherKinds", "-language:reflectiveCalls", "-language:postfixOps"),
  javacOptions ++= Seq("-target", "1.8", "-source", "1.8"),
  //    manifestSetting,
  //    publishSetting,
  resolvers ++= Seq(Opts.resolver.sonatypeSnapshots, Opts.resolver.sonatypeReleases),
  crossVersion := CrossVersion.binary,
  resolvers ++= Seq(
    "spray repo" at "http://repo.spray.io/",
    "sonatype releases" at "http://oss.sonatype.org/content/repositories/releases/",
    "sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
    "typesafe repo" at "http://repo.typesafe.com/typesafe/releases/"
  )
)

lazy val root = (project in file("."))
  .aggregate(gcs, server)
  .settings(
    shared,
    name := "edisko"
  )

lazy val gcs = (project in file("edo-gcs"))
  .settings(
    shared,
    name := "edo-gcs",
    libraryDependencies ++= Seq(
      "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
      "ch.qos.logback" % "logback-classic" % "1.2.3",

      "com.google.cloud" % "google-cloud-translate" % "1.39.0",

      "com.typesafe.akka" %% "akka-http" % akkaHttpV,
      "com.typesafe.akka" %% "akka-http-core" % akkaHttpV,

      // Config file parser
      "com.github.pureconfig" %% "pureconfig" % "0.9.1",

      // JSON serialization library
      "io.circe" %% "circe-core" % circeV,
      "io.circe" %% "circe-generic" % circeV,
      "io.circe" %% "circe-parser" % circeV,

      // Sugar for serialization and deserialization in akka-http with circe
      "de.heikoseeberger" %% "akka-http-circe" % "1.20.1",

      // Validation library
      "com.wix" %% "accord-core" % "0.7.1",

      "org.scalatest" %% "scalatest" % scalaTestV % Test,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV % Test
    )
  )

lazy val server = (project in file("edo-server"))
  .dependsOn(gcs)
  .settings(
    shared,
    name := "edo-server",
    libraryDependencies ++= Seq(

      "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
      "ch.qos.logback" % "logback-classic" % "1.2.3",

      "org.rogach" %% "scallop" % "3.1.5",

      "com.typesafe.akka" %% "akka-http" % akkaHttpV,
      "com.typesafe.akka" %% "akka-http-core" % akkaHttpV,

      // Support of CORS requests, version depends on akka-http
      "ch.megard" %% "akka-http-cors" % "0.3.0",

      // SQL generator
      "com.typesafe.slick" %% "slick" % slickVersion,

      // Postgres driver
      "org.postgresql" % "postgresql" % "42.1.4",

      // Migration for SQL databases
      "org.flywaydb" % "flyway-core" % "4.2.0",

      // Connection pool for database
      "com.zaxxer" % "HikariCP" % "2.7.0",

      // Encoding decoding sugar, used in passwords hashing
      "com.roundeights" %% "hasher" % "1.2.0",

      // Parsing and generating of JWT tokens
      "com.pauldijou" %% "jwt-core" % "0.16.0",

      // Config file parser
      "com.github.pureconfig" %% "pureconfig" % "0.9.1",

      // JSON serialization library
      "io.circe" %% "circe-core" % circeV,
      "io.circe" %% "circe-generic" % circeV,
      "io.circe" %% "circe-parser" % circeV,

      // Sugar for serialization and deserialization in akka-http with circe
      "de.heikoseeberger" %% "akka-http-circe" % "1.20.1",

      // Validation library
      "com.wix" %% "accord-core" % "0.7.1",

      // Http client, used currently only for IT test
      "com.softwaremill.sttp" %% "core" % sttpV % Test,
      "com.softwaremill.sttp" %% "akka-http-backend" % sttpV % Test,

      "org.scalatest" %% "scalatest" % scalaTestV % Test,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV % Test,
      "ru.yandex.qatools.embed" % "postgresql-embedded" % "2.9" % Test,
      "org.mockito" % "mockito-all" % "1.9.5" % Test
    )
  )
