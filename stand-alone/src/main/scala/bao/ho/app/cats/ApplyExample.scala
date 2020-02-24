package bao.ho.app.cats

import cats.effect.{ExitCode, IO, IOApp}

object ApplyExample extends IOApp {
  import cats.syntax.applicative._
  import cats.syntax.applicativeError._
  import cats.syntax.apply._

  def func(i: Int): IO[Unit] =
    IO.raiseError(new Exception("Hello 1")).whenA(i == 1) *>
      IO.raiseError(new Exception("Hello 2")).whenA(i == 2) *>
      IO.raiseError(new Exception("Hello 3")).whenA(i == 3)

  override def run(args: List[String]): IO[ExitCode] =
    func(5)
      .handleError(e => println(e))
      .map(_ => ExitCode.Success)
}
