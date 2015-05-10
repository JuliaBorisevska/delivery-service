name := """deliveryservice"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"


libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa,
  cache,
  javaWs,
  "mysql" % "mysql-connector-java" % "5.1.18",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final",
  "be.objectify" %% "deadbolt-java" % "2.3.1",
  "org.powermock" % "powermock-api-mockito" % "1.5.1" % "test",
  "org.powermock" % "powermock-module-javaagent" % "1.5.1" % "test",
  "org.powermock" % "powermock-module-junit4-rule-agent" % "1.5.1" % "test",
  "com.rabbitmq" % "amqp-client" % "2.8.1",
  "org.apache.commons" % "commons-email" % "1.3.3",
  "com.github.jsr-330" % "core" % "1.4.0",
  "com.google.inject" % "guice" % "3.0",
  "com.typesafe.play" % "play-mailer_2.10" % "2.4.0-RC1",
  "org.antlr" % "ST4" % "4.0.7"
)

resolvers += Resolver.url("Objectify Play Repository", url("http://deadbolt.ws/releases/"))(Resolver.ivyStylePatterns)
