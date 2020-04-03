package bao.ho.app.ucats.concurrent

import java.util.concurrent.Executors

import cats.effect.{ContextShift, ExitCode, Fiber, IO, IOApp}

import scala.concurrent.ExecutionContext

object RepeatExample extends IOApp {
  import cats.syntax.apply._
//  import cats.syntax.parallel._
//  import cats.syntax.traverse._
//  import cats.syntax.functor._
//  import cats.syntax.applicative._
//  import cats.syntax.flatMap._
  val ecOne = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())
  val ecTwo = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

  val csOne: ContextShift[IO] = IO.contextShift(ecOne)
  val csTwo: ContextShift[IO] = IO.contextShift(ecTwo)

  def infiniteIO(id: Int)(cs: ContextShift[IO]): IO[Fiber[IO, Unit]] = {
//    def repeat: IO[Unit] = IO(println(id)).flatMap(_ => repeat)
    def repeat: IO[Unit] =
      IO(println(s"${Thread.currentThread().getName()} -- ${id} ")).flatMap(_ => IO.shift *> repeat)

    repeat.start(cs)
  }

  override def run(args: List[String]): IO[ExitCode] = {
    val program = {
      import cats.implicits._
      List((1, csOne), (11, csOne), (2, csOne), (22, csOne), (55, csOne))
        .map(i => infiniteIO(i._1)(i._2))
        .parSequence
    }
    //      List(infiniteIO(1)(csOne), infiniteIO(11)(csOne), infiniteIO(2)(csTwo), infiniteIO(22)(csTwo)).parTraverse
//      for {
//        _ <- infiniteIO(1)(csOne)
//        _ <- infiniteIO(11)(csOne)
//        _ <- infiniteIO(2)(csTwo)
//        _ <- infiniteIO(22)(csTwo)
//      } yield ()

    program *> IO(ExitCode.Success)
  }
}
