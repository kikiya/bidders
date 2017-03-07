lazy val root = (project in file("."))
  .settings(name := "online-auction-scala")
  .aggregate(itemImpl,
    biddingApi, biddingImpl
)
  .settings(commonSettings: _*)

organization in ThisBuild := "com.example"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

val playJsonDerivedCodecs = "org.julienrf" %% "play-json-derived-codecs" % "3.3"
val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % "test"

lazy val itemImpl = lagomExternalScaladslProject("item", "com.example" %% "itemimpl" % "1.0-SNAPSHOT")

lazy val security = (project in file("security"))
  .settings(commonSettings: _*)
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      lagomScaladslServer % Optional,
      playJsonDerivedCodecs,
      scalaTest
    )
  )


lazy val biddingApi = (project in file("bidding-api"))
  .settings(commonSettings: _*)
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      playJsonDerivedCodecs
    )
  )
  .dependsOn(security)

lazy val biddingImpl = (project in file("bidding-impl"))
  .settings(commonSettings: _*)
  .enablePlugins(LagomScala)
  .dependsOn(biddingApi)
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      lagomScaladslKafkaBroker,
      macwire,
      scalaTest,
      "com.example" %% "itemapi" % "1.0-SNAPSHOT"
    ),
    maxErrors := 10000

  )


def commonSettings: Seq[Setting[_]] = Seq(
)

lagomCassandraCleanOnStart in ThisBuild := false


