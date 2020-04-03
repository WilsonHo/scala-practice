package bao.ho.app.ucats

import java.util.concurrent.Executors

import cats.Monad
import cats.effect.{ContextShift, Fiber, IO}
import cats.syntax.apply._

import scala.concurrent.ExecutionContext

object IOExample {

  implicit val ec: ExecutionContext =
    ExecutionContext.fromExecutor(Executors.newFixedThreadPool(2))

  implicit val cs: ContextShift[IO] = IO.contextShift(ec)

  def taskHeavy(prefix: String): IO[Nothing] =
    Monad[IO].tailRecM(0) { i =>
      for {
        _ <- IO { println(s"${Thread.currentThread.getName}; $prefix: $i") }
        _ <- IO { Thread.sleep(1000) }
      } yield Left(i + 1)
    }

  def bunch(n: Int)(gen: String => IO[Nothing]): IO[List[Fiber[IO, Nothing]]] = {
    import cats.syntax.traverse._
    import cats.instances.list._
    (1 to n).toList.map(i => s"Task $i").traverse(gen(_).start)
  }

  def run1: List[Fiber[IO, Nothing]] = (IO.shift *> bunch(1000)(taskHeavy)).unsafeRunSync

  def main(args: Array[String]): Unit = {
    run1
    println("Demo")
  }
}
