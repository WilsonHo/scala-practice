package bao.ho.app.ucats.concurrent

import cats.effect.concurrent.MVar
import cats.effect.{ExitCode, IO, IOApp}

object MVarExample extends IOApp {
  import cats.syntax.apply._

  def sum(state: MVar[IO, Int], list: List[Int]): IO[Int] =
    list match {
      case Nil => state.take
      case x :: xs =>
        state.take.flatMap { current =>
          state.put(current + x).flatMap(_ => sum(state, xs))
        }
    }

  override def run(args: List[String]): IO[ExitCode] =
    MVar.of[IO, Int](0).flatMap(sum(_, (0 until 100).toList)) *> IO(ExitCode.Success)
}
