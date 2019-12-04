package bao.ho.app

import org.scalatra.test.scalatest._

class ExampleControllerTests extends ScalatraFunSuite {
  addServlet(classOf[ExampleController], "/*")

  test("GET / on MyScalatraServlet should return status 200") {
    get("/") {
      status should equal(200)
    }
  }
}
