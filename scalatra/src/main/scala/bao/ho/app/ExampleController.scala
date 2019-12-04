package bao.ho.app

import org.scalatra._

class ExampleController extends ScalatraServlet {
  get("/") {
    "Hello World 4"
  }
}
