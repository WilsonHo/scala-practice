package bao.ho.app

import cats.effect.{ExitCode, IO, IOApp}
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger

object Main extends IOApp {
  import cats.syntax.flatMap._

  override def run(args: List[String]): IO[ExitCode] = {
    val log = for {
      _      <- IO.delay(println("Start"))
      logger <- Slf4jLogger.create[IO]
      _      <- logger.info("I'm logger!!")
    } yield ()

    log >> IO(ExitCode.Success)
  }
}
