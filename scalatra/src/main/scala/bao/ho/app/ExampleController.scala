package bao.ho.app

import com.codahale.metrics.Meter
import org.scalatra._
import org.scalatra.metrics.MetricsSupport

class ExampleController(requestMetric: Meter)
    extends ScalatraServlet
    with MetricsSupport
    with ExampleOneController {
//  private val requests: Meter = metrics.meter("requests")

  get("/") {
    timer("timer") {
      Thread.sleep(1000)
    }

    // Increments a counter called "counter"
    counter("counter") += 1

    // Increments a histogram called "histogram"
    histogram("histogram") += 1

    // Sets a gauge called "gauge"
//    gauge("gauge") {
//      "gauge"
//    }

    meter("meter").mark(1)
    requestMetric.mark()
    "Hello World 4"
  }
}
