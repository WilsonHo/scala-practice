package bao.ho.app.fs2cron

import cats.effect.concurrent.{Deferred, MVar}
import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream

import scala.concurrent.duration._
import cats.syntax.apply._
import cats.syntax.functor._

object SlowDownExample extends IOApp {
  val showSlowDown = Stream.eval(IO.delay(println("----------Slow Down----------")))
  val showReset    = Stream.eval(IO.delay(println("----------Reset----------")))

//  def slowDownEveryNTicks(resets: Stream[IO, Unit], n: Long): Stream[IO, FiniteDuration] = {
//    val delayExponential: Stream[IO, FiniteDuration] =
//      Stream
//        .iterate(1.milliseconds)(_ * 2)
//        .flatMap(s => Stream.awakeDelay[IO](s).take(n) ++ showSlowDown)
//
//    Stream.eval(MVar.empty[IO, Unit]).flatMap { restart =>
//      val delayUntilReset = delayExponential.interruptWhen(restart.take.attempt)
//
//      delayUntilReset.repeat.concurrently(resets.evalMap(_ => restart.put()) *> showReset)
//    }
//  }
//
//  val program =
//    Stream.eval(Deferred[IO, Unit]).flatMap { switch =>
//      val switcher =
//        Stream.eval(switch.complete(())).delayBy(5.seconds)
//
//      val program =
//        Stream.repeatEval(IO(println(java.time.LocalTime.now))).metered(1.second)
//
//      program.repeat
//        .interruptWhen(switch.get.attempt)
//        .concurrently(switcher)
//    }

//  val switcher =
//    Stream.eval(switch.complete(())).delayBy(5.seconds)

//  val stream = Stream.apply(1, 2, 3).apply[IO]

  val executor = Stream.repeatEval(IO(println(java.time.LocalTime.now))).metered(1.second)

  val defer = Stream.eval(Deferred[IO, Unit])

  override def run(args: List[String]): IO[ExitCode] =
    Stream
      .eval(Deferred[IO, Unit])
      .flatMap { switch =>
        val switcher =
          Stream.eval(switch.complete(())).delayBy(5.seconds)

        val program =
          Stream.awakeEvery[IO](1.second) *> Stream.eval(IO(println(java.time.LocalTime.now)))
//          Stream.repeatEval(IO(println(java.time.LocalTime.now))).metered(1.second)

        val x = program
          .interruptWhen(switch.get.attempt)
          .concurrently(switcher)

        x.repeat
      }
      .compile
      .drain
      .as(ExitCode.Success)
}
