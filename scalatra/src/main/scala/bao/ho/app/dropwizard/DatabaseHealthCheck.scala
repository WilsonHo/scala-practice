package bao.ho.app.dropwizard

import com.codahale.metrics.health.HealthCheck

class DatabaseHealthCheck(database: Database) extends HealthCheck {

  override def check(): HealthCheck.Result =
    if (database.isConnected()) {
      HealthCheck.Result.healthy();
    } else {
      HealthCheck.Result.unhealthy("Cannot connect to " + database.getUrl());
    }
}
