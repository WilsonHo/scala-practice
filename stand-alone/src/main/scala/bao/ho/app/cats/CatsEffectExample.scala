package bao.ho.app.cats

import cats.effect.IO

object CatsEffectExample {
  def d1: IO[Long]          = IO.delay(100)
  def d2: IO[Unit]          = IO.raiseError(new RuntimeException("Bao"))
  def d3(a: Long): IO[Long] = IO.delay(a + 1)

  def main(args: Array[String]): Unit = {
    import cats.syntax.applicative._

    val a: Option[String] = Some("a")
    val b: Option[String] = None

    import cats.implicits._
    val k = (a, b).mapN {
      case (m1, m2) => println(m1 + m2)
    }
    k.fold(println("Bao"))(identity)

//    val run = for {
//      v1 <- d1
//      _  <- d2
//      re <- d3(v1)
//    } yield re
////      IO.delay(println("Hello"))
//    run
//      .whenA(true)
//      .whenA(true)
//      .unsafeRunSync()
  }
}
