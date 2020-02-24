import bao.ho.app._
import bao.ho.app.dropwizard.{Database, DatabaseHealthCheck}
import com.codahale.metrics.Meter
import org.scalatra._
import javax.servlet.ServletContext
import org.scalatra.metrics.MetricsBootstrap
import org.scalatra.metrics.MetricsSupportExtensions._
import com.codahale.metrics.servlet._

class ScalatraBootstrap extends LifeCycle with MetricsBootstrap {

//  override val metricRegistry =
  override def init(context: ServletContext): Unit = {
    val requestMetric: Meter = metricRegistry.meter("requests")
    context.mount(new ExampleController(requestMetric), "/example")
    context.mount(new DownloadController, "/download")

    context.mountMetricsAdminServlet("/metrics-admin")
    context.mountHealthCheckServlet("/health")
    context.mountMetricsServlet("/metrics")
    context.mountThreadDumpServlet("/thread-dump")
    context.installInstrumentedFilter("/example/")

    val database = new Database
    healthCheckRegistry.register("mysql", new DatabaseHealthCheck(database))
  }
}
