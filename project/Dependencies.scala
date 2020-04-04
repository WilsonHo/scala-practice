import Dependencies.ExclusionRules
import sbt._

object Dependencies {

  object Versions {
    val sparkVersion          = "2.4.0"
    val scalatraJson4sV       = "3.5.2"
    val json4sSpark2_4_0_V    = "3.5.3"
    val scalaTestV            = "3.0.8"
    val hadoopV               = "2.7.0-mapr-1808"
    val jettyWebAppV          = "9.4.14.v20181114"
    val log4jV                = "2.12.0"
    val orgSlf4jV             = "1.7.25"
    val mysqlConnectorJavaV   = "5.1.34"
    val h2V                   = "1.4.199"
    val scalatraFrameworkV    = "2.6.+"
    val circeV                = "0.12.1"
    val circeConfigV          = "0.7.0-M1"
    val doobieV               = "0.8.0-M1"
    val enumeratumCirceV      = "1.5.21"
    val enumeratumQuillV      = "1.5.14"
    val flywayV               = "5.2.4"
    val kindProjectorV        = "0.10.3"
    val log4catV              = "0.4.0-M2"
    val scalaCheckV           = "1.14.0"
    val catsScalaCheckV       = "0.2.0-M1"
    val http4sV               = "0.20.7"
    val pureConfigV           = "0.11.1"
    val circeShapesV          = "0.7.0"
    val catsV                 = "2.0.0"
    val mouseV                = "0.23"
    val chimneyV              = "0.3.3"
    val osLib                 = "0.3.0"
    val scalaMockV            = "4.4.0"
    val tookitakiCommonsAuthV = "0.0.20"
    val mariadbJavaClientV    = "2.5.4"
    val monocleV              = "2.0.0"
  }

  import Versions._

  val scalatraApiDependencies = Seq(
    "org.scalatra"      %% "scalatra"           % scalatraFrameworkV,
    "org.eclipse.jetty" % "jetty-webapp"        % "9.4.9.v20180320" % "container;compile",
    "javax.servlet"     % "javax.servlet-api"   % "3.1.0" % Provided,
    "org.scalatra"      %% "scalatra-json"      % scalatraFrameworkV,
    "org.scalatra"      %% "scalatra-swagger"   % scalatraFrameworkV,
    "org.json4s"        %% "json4s-native"      % "3.5.3",
    "org.scalatra"      %% "scalatra-scalatest" % scalatraFrameworkV % Test,
    "ch.qos.logback"    % "logback-classic"     % "1.2.3" % "runtime",
    "org.scalatra"      %% "scalatra-metrics"   % "2.6.5"
  )

  val catsDependencies = Seq(
    "org.typelevel" %% "cats-core"   % catsV,
    "org.typelevel" %% "cats-free"   % catsV,
    "org.typelevel" %% "cats-effect" % catsV,
    "org.typelevel" %% "mouse"       % mouseV
  )

  val circeDepedencies = Seq(
    "io.circe"     %% "circe-core"           % circeV,
    "io.circe"     %% "circe-generic"        % circeV,
    "io.circe"     %% "circe-literal"        % circeV,
    "io.circe"     %% "circe-parser"         % circeV,
    "io.circe"     %% "circe-generic-extras" % circeV,
    "io.circe"     %% "circe-config"         % circeConfigV,
    "com.beachape" %% "enumeratum-circe"     % enumeratumCirceV,
    "com.beachape" %% "enumeratum-quill"     % enumeratumQuillV
  )

  val commonsIdDependencies = Seq("commons-io" % "commons-io" % "2.6")

  val jMimeMagicDependencies = Seq("net.sf.jmimemagic" % "jmimemagic" % "0.1.5")

  val dropwizardDependencies = Seq(
    "io.dropwizard.metrics" % "metrics-core"         % "4.1.2",
    "io.dropwizard.metrics" % "metrics-healthchecks" % "4.1.2"
  )

  val log4j2Dependencies = Seq(
    "org.apache.logging.log4j" % "log4j-api"  % "2.13.0",
    "org.apache.logging.log4j" % "log4j-core" % "2.13.0"
  )

  val log4catsDependencies = Seq(
    "io.chrisdavenport"        %% "log4cats-slf4j"  % "1.0.1",
    "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.13.1"
  )

  object ExclusionRules {
    val json4S                = ExclusionRule(organization = "org.json4s")
    val jackson               = ExclusionRule(organization = "com.fasterxml.jackson.core")
    val javaxServlet          = ExclusionRule(organization = "javax.servlet")
    val orgSlf4j              = ExclusionRule(organization = "org.slf4j")
    val ldapApi               = ExclusionRule(organization = "org.apache.directory.api")
    val orgMortbayJetty       = ExclusionRule(organization = "org.mortbay.jetty")
    val comGoogleCodeFindbugs = ExclusionRule(organization = "com.google.code.findbugs")
    val orgXerial             = ExclusionRule(organization = "org.xerial")
    val awsJdk                = ExclusionRule(organization = "com.amazonaws")
    val spark                 = ExclusionRule(organization = "org.apache.spark")
    val hikari                = ExclusionRule(organization = "com.zaxxer")
    val asm                   = ExclusionRule(organization = "asm")
  }

  val akkaDependencies = Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.6.3"
  )

  val jsoupDependencies = Seq(
    "org.jsoup" % "jsoup" % "1.12.2"
  )

  val fs2Dependencies = Seq(
    "co.fs2"     %% "fs2-core"      % "2.2.1",
    "co.fs2"     %% "fs2-io"        % "2.2.1",
    "eu.timepit" %% "fs2-cron-core" % "0.2.2"
  )

  val livyDependencies = Seq(
    "org.apache.livy" % "livy-client-http"    % "0.7.0-incubating",
    "org.apache.livy" % "livy-api"            % "0.7.0-incubating",
    "org.apache.livy" % "livy-rsc"            % "0.7.0-incubating",
    "org.apache.livy" % "livy-core_2.11"      % "0.7.0-incubating",
    "org.apache.livy" % "livy-scala-api_2.11" % "0.7.0-incubating"
  )

  val googleDependencies = Seq(
    "com.google.guava" % "guava" % "16.0.1"
  )

  val monocleDependencies = Seq(
    "com.github.julien-truffaut" %% "monocle-core"  % monocleV,
    "com.github.julien-truffaut" %% "monocle-macro" % monocleV,
    "com.github.julien-truffaut" %% "monocle-law"   % monocleV % "test"
  )

  val hadoopDependencies = Seq(
    "org.apache.hadoop" % "hadoop-common" % "2.6.3",
    "org.apache.hadoop" % "hadoop-common" % "2.6.3",
    "org.apache.hadoop" % "hadoop-core"   % "1.2.1"
  )
}
