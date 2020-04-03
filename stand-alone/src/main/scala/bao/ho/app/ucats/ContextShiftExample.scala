package bao.ho.app.ucats

import java.util.concurrent.Executors

import scala.concurrent.ExecutionContext

object ContextShiftExample {
  import cats.effect._
  import cats.implicits._

  implicit val ec: ExecutionContext =
    ExecutionContext.fromExecutor(Executors.newFixedThreadPool(4))

  implicit val cs: ContextShift[IO] = IO.contextShift(ec)

  def p[F[_]](implicit F: Sync[F]) = F.delay { println(s"${Thread.currentThread.getName}") }

  def fib[F[_]](n: Int, a: Long = 0, b: Long = 1)(
    implicit F: Sync[F],
    cs: ContextShift[F]
  ): F[Long] =
    F.suspend {
      val next =
        if (n > 0) fib(n - 1, b, a + b)
        else F.pure(a)

      // Triggering a logical fork every 100 iterations

      if (n % 100 == 0)
        cs.shift *> p *> next
      else
        next
    }

  def blockingThreadPool[F[_]](implicit F: Sync[F]): Resource[F, ExecutionContext] =
    Resource(F.delay {
      val executor = Executors.newCachedThreadPool()
      val ec       = ExecutionContext.fromExecutor(executor)
      (ec, F.delay(executor.shutdown()))
    })

  def readName[F[_]](implicit F: Sync[F]): F[String] =
    p *> F.delay {
      println("Enter your name: ")
      scala.io.StdIn.readLine()
    }

  def readName[F[_]: Sync: ContextShift](blocker: Blocker): F[String] =
    // Blocking operation, executed on special thread-pool
    p *> blocker.delay {
      println("Enter your name: ")
      scala.io.StdIn.readLine()
    }

  def run1 = {
    val name = blockingThreadPool[IO].use { ec =>
      // Blocking operation, executed on special thread-pool
      cs.evalOn(ec)(readName[IO])
    }

    for {
      n <- name
      _ <- IO(println(s"Hello, $n!"))
    } yield ExitCode.Success
  }

  def run2 = {
    val name = Blocker[IO].use { blocker =>
      readName[IO](blocker)
    }

    for {
      n <- name
      _ <- IO(println(s"Hello, $n!"))
    } yield ExitCode.Success
  }

  def main(args: Array[String]): Unit = {
//    run.unsafeRunSync()
//    fib[IO](10, 10, 5).unsafeRunSync()
//    fib[IO](100, 10, 5).unsafeRunSync()
//    fib[IO](110, 10, 5).unsafeRunSync()
//    fib[IO](510, 10, 5).unsafeRunSync()
    run2.unsafeRunSync()
    println(s"Demo")
  }
}
