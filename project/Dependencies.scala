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
  }

  import Versions._

  val scalatraApiDependencies = Seq(
    "org.scalatra"      %% "scalatra"         % scalatraFrameworkV,
    "org.eclipse.jetty" % "jetty-webapp"      % "9.4.9.v20180320" % "container;compile",
    "javax.servlet"     % "javax.servlet-api" % "3.1.0" % Provided,
    "org.scalatra"      %% "scalatra-json"    % scalatraFrameworkV,
    "org.scalatra"      %% "scalatra-swagger" % scalatraFrameworkV,
    "org.json4s"        %% "json4s-native"    % "3.5.3"
  )

  val catsDependencies = Seq(
    "org.typelevel" %% "cats-core"   % catsV,
    "org.typelevel" %% "cats-free"   % catsV,
    "org.typelevel" %% "cats-effect" % catsV,
    "org.typelevel" %% "mouse"       % mouseV
  )
}
