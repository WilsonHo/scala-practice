package bao.ho.app.fs2cron

import cats.effect.concurrent.Deferred
import cats.effect.{Effect, ExitCode, IO, IOApp, Sync}
import fs2.Stream
import fs2.concurrent.Signal

object Fs2Example extends IOApp {

  object First extends FsExample[IO, Unit] {
    import cats.effect._
    import scala.concurrent.duration._

    override def worker(args: List[String]): Stream[IO, Unit] =
      for {
        _ <- Stream.awakeEvery[IO](1.second)
        _ <- Stream.eval(IO(println("Hello")))
        a <- Stream.emit(100)
        _ <- Stream.eval(IO(println(a)))
      } yield ()
  }

  object Second extends FsExample[IO, Unit] {
    import cats.effect._
    import scala.concurrent.duration._

    override def worker(args: List[String]): Stream[IO, Unit] =
      (Stream.eval(IO(println("Hello"))) ++ Stream.sleep(5.second)).repeat
  }

  object Third extends FsExample[IO, Unit] {
    import cats.effect._
    import scala.concurrent.duration._

    def destroyUniverse(): Unit = println("BOOOOM!!!");
    Stream.eval_(IO { destroyUniverse() }) ++ Stream("...moving on")

    override def worker(args: List[String]): Stream[IO, Unit] =
      Stream.eval(Deferred[IO, Unit]).flatMap { switch =>
        val switcher =
          Stream.eval(switch.complete(())).delayBy(20.seconds)

        val program =
          Stream.repeatEval(IO(println(java.time.LocalTime.now))).metered(1.second)

        program
          .interruptWhen(switch.get.attempt)
          .concurrently(switcher)
      }
  }

  object Four extends FsExample[IO, Unit] {
    import cats.effect._
    import scala.concurrent.duration._

    def destroyUniverse(): Unit = println("BOOOOM!!!")

    def myprint(): Stream[IO, Unit] =
      Stream.eval_(IO { destroyUniverse() }) ++ myprint().delayBy(2.second)

    //    Stream.eval_(IO { destroyUniverse() }) ++ Stream("...moving on")

    override def worker(args: List[String]): Stream[IO, Unit] =
      myprint()
  }
  override def run(args: List[String]): IO[ExitCode] = Four.exec(args)
}
