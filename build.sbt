name := "template-project"

version := "1.0.1"

lazy val `template-project` = (project in file("."))
  .enablePlugins(PlayScala, SwaggerPlugin)

libraryDependencies ++= Seq(
    // java/sbt/play basics
    jdbc, ws, ehcache, filters, specs2 % Test, guice,
    "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test,
    "org.webjars" % "swagger-ui" % "3.22.2",
    "net.sourceforge.jtds" % "jtds" % "1.2.6",

    // logging libraries
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    "net.logstash.logback" % "logstash-logback-encoder" % "6.2",

    // other tools
    "com.github.mumoshu" %% "play2-memcached-play27" % "0.10.0" exclude("net.spy", "spymemcached"),
    "com.newmotion" %% "akka-rabbitmq" % "5.1.1"
)

unmanagedResourceDirectories in Test += baseDirectory( _ /"target/web/public/test" ).value

resolvers ++= Seq(
    "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/",
    "Spy Repository" at "http://files.couchbase.com/maven2",
    "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
)

swaggerDomainNameSpaces := Seq("xyz.meggls.template")

routesGenerator := InjectedRoutesGenerator