package bao.ho.app.dropwizard

import com.codahale.metrics.ConsoleReporter
import java.util.concurrent.TimeUnit

object MeterExample {
  import com.codahale.metrics.MetricRegistry

  private val metrics = new MetricRegistry

  def startReport() = {
    val reporter = ConsoleReporter
      .forRegistry(metrics)
      .convertRatesTo(TimeUnit.SECONDS)
      .convertDurationsTo(TimeUnit.MILLISECONDS)
      .build
    reporter.start(1, TimeUnit.SECONDS)
  }

  def wait5Seconds() =
    try Thread.sleep(5 * 1000)
    catch {
      case e: InterruptedException => println(e)
    }

  def main(args: Array[String]): Unit = {
    startReport()
    val requests = metrics.meter("requests")
    val demo     = metrics.meter("demo")
    demo.mark()
    demo.mark()
    requests.mark()
    requests.mark()
    requests.mark()
    requests.mark()
    requests.mark()
    wait5Seconds()
  }
}
