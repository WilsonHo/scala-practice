package bao.ho.app

import org.scalatra._
import org.scalatra.metrics.MetricsSupport

trait ExampleOneController {
  self: ScalatraServlet with MetricsSupport =>
//  private val requests: Meter = metrics.meter("requests")

  get("/demo") {
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
    "Hello World 3"
  }
}
