package bao.ho.app.ucats

import cats.effect.{ContextShift, ExitCase, ExitCode, IO, IOApp}
import cats.syntax.apply._

import scala.concurrent.ExecutionContext

object FiberExample extends IOApp {
//  def main(args: Array[String]): Unit = {
//    implicit val contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
//

//  }
  override def run(args: List[String]): IO[ExitCode] = {
    val launchMissiles: IO[Unit] = IO.delay(println("Go!"))
//    val launchMissiles: IO[Unit] = IO.raiseError(new Exception("boom!"))
//    val runToBunker = IO(println("To the bunker!!!"))
    val runToBunker: IO[Unit] = IO.raiseError(new Exception("Error!"))

    val a = for {
      fiber <- launchMissiles.start
      _ <- runToBunker.handleErrorWith { error =>
            // Retreat failed, cancel launch (maybe we should
            // have retreated to our bunker before the launch?)
            println("---- " + error)
            fiber.cancel *> IO.raiseError(error)
          }
      aftermath <- fiber.join
    } yield aftermath

    a.map(_ => ExitCode.Success)
  }
}
