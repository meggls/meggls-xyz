name := "meggls-xyz"

version := "1.0.1"

lazy val `meggls-xyz` = (project in file("."))
  .enablePlugins(PlayScala, SwaggerPlugin)

libraryDependencies ++= Seq(
    // java/sbt/play basics
    ws, filters, specs2 % Test, guice,
    "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test,
    "org.webjars" % "swagger-ui" % "3.22.2",

    // db
    jdbc,
    "com.typesafe.play" %% "anorm" % "2.5.3",
    "mysql" % "mysql-connector-java" % "8.0.19",

    // logging libraries
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    "net.logstash.logback" % "logstash-logback-encoder" % "6.2",
)

unmanagedResourceDirectories in Test += baseDirectory( _ /"target/web/public/test" ).value

resolvers ++= Seq(
    "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/",
    "Spy Repository" at "http://files.couchbase.com/maven2",
    "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
)

swaggerDomainNameSpaces := Seq("xyz.meggls")

routesGenerator := InjectedRoutesGenerator