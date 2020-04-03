package bao.ho.app.ucats.concurrent

import cats.effect.concurrent.Deferred
import cats.effect.{ExitCode, IO, IOApp}

object DeferredExample extends IOApp {
  import cats.implicits._

  def start(d: Deferred[IO, Int]): IO[Unit] = {
    val attemptCompletion: Int => IO[Unit] = n => d.complete(n).attempt.void

    List(
      IO.race(attemptCompletion(1), attemptCompletion(2)),
      d.get.flatMap { n =>
        IO(println(s"Result: $n"))
      }
    ).parSequence.void
  }

  override def run(args: List[String]): IO[ExitCode] = {
    val program: IO[Unit] =
      for {
        d <- Deferred[IO, Int]
        _ <- start(d)
      } yield ()
    program *> IO(ExitCode.Success)
  }
}
