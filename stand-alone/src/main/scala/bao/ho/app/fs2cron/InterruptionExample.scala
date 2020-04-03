package bao.ho.app.fs2cron

import cats.effect.concurrent.Deferred
import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream

import scala.concurrent.duration._

object InterruptionExample extends IOApp {
  import scala.concurrent.duration._
  import cats.syntax.apply._
  import cats.syntax.functor._

  val program =
    Stream.eval(Deferred[IO, Unit]).flatMap { switch =>
      val switcher = for {
        _ <- Stream.eval(switch.complete(())).delayBy(5.seconds)
        _ <- Stream.eval(IO(println("******* REPEAT *******")))
      } yield ()

      val inside = for {
        i <- Stream
              .iterate(1.nanosecond)(_ * 2)
              .covary[IO]
        _ <- Stream
              .eval(IO(println(s"Running :: ${java.time.LocalTime.now} # ${i}")))
              .delayBy[IO](i)
        //Stream.repeatEval(IO(println())).metered(du)
      } yield ()

      inside
        .interruptWhen(switch.get.attempt)
        .concurrently(switcher)
    }

  override def run(args: List[String]): IO[ExitCode] = {
    import cats.implicits._
    val p = for {
      _ <- program.repeat
    } yield ()
    p.compile.drain *> IO(ExitCode.Success)
  }
}
