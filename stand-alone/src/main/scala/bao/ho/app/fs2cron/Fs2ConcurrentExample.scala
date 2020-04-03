package bao.ho.app.fs2cron

import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream

object Fs2ConcurrentExample extends IOApp {
//  sealed trait Example[F[_], A] {
//    def worker(args: List[String]): Stream[F, A]
//
//    def exec(args: List[String]): F[ExitCode] = worker(args).compile.drain.as(ExitCode.Success)
//  }
//
//  object QueueExample extends Example[IO, Unit] {
//    import ucats.effect.IO
//    import fs2.concurrent.Queue
//    import fs2.Stream
//
//    import scala.concurrent.duration._
//
//    class Buffering(q1: Queue[IO, Int], q2: Queue[IO, Int]) {
//
//      def start: Stream[IO, Unit] =
//        Stream(
//          Stream.range(0, 1000).covary[IO].through(q1.enqueue),
//          q1.dequeue.through(q2.enqueue),
//          //.map won't work here as you're trying to map a pure value with a side effect. Use `evalMap` instead.
//          q2.dequeue.evalMap(n => IO.delay(println(s"Pulling out $n from Queue #2")))
//        ).parJoin(3)
//    }
//
//    override def worker(args: List[String]): Stream[IO, Unit] =
//      for {
//        q1 <- Stream.eval(Queue.bounded[IO, Int](1))
//        q2 <- Stream.eval(Queue.bounded[IO, Int](1))
//        bp = new Buffering(q1, q2)
//        _  <- Stream.sleep_[IO](5.seconds).concurrently(bp.start.drain)
//      } yield ()
//  }
  override def run(args: List[String]): IO[ExitCode] = ??? //QueueExample.exec(args)
}
