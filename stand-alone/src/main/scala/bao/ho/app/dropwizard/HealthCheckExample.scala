package bao.ho.app.dropwizard

import com.codahale.metrics.health.HealthCheck
import com.codahale.metrics.health.HealthCheckRegistry

object HealthCheckExample {

  class Database {
    def isConnected() = false
    def getUrl()      = "jdbc:mysql://localhost:3306/baoho"
  }

  class DatabaseHealthCheck(database: Database) extends HealthCheck {

    override def check(): HealthCheck.Result =
      if (database.isConnected()) {
        HealthCheck.Result.healthy();
      } else {
        HealthCheck.Result.unhealthy("Cannot connect to " + database.getUrl());
      }
  }

  def main(args: Array[String]): Unit = {
    import collection.JavaConverters._

    val healthChecks = new HealthCheckRegistry
    val database     = new Database
    healthChecks.register("mysql", new HealthCheckExample.DatabaseHealthCheck(database))

    val results = healthChecks.runHealthChecks.asScala
    results.foreach {
      case (key, value) =>
        if (value.isHealthy) println(key + " is healthy")
        else {
          println(key + " is UNHEALTHY: " + value.getMessage)
          val e: Throwable = value.getError
          if (e != null) {
            e.printStackTrace();
          }
        }
    }
  }
}
