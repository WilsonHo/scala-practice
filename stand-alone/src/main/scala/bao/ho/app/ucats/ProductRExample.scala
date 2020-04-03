package bao.ho.app.ucats

import cats.effect.IO
import cats.implicits._

import scala.language.postfixOps

object ProductRExample {
//  implicit val ec: ExecutionContext =
//    ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1))
//
//  implicit val cs: ContextShift[IO] = IO.contextShift(ec)
//
//  implicit val timer = IO.timer(ec)
//
//  def bunch(n: Int)(gen: String => IO[Nothing]): IO[List[Fiber[IO, Nothing]]] =
//    (1 to n).toList.map(i => s"Task $i").traverse(gen(_).start)
//
//  def taskHeavy(prefix: String): IO[Nothing] =
//    Monad[IO].tailRecM(0) { i =>
//      for {
//        _ <- IO { println(s"${Thread.currentThread.getName}; $prefix: $i") }
//        _ <- IO { Thread.sleep(1000) }
//      } yield Left(i + 1)
//    }
//
//  def taskLight(prefix: String): IO[Nothing] =
//    Monad[IO].tailRecM(0) { i =>
//      for {
//        _ <- IO { println(s"${Thread.currentThread.getName}; $prefix: $i") }
//        _ <- IO.sleep(1 second)
//      } yield Left(i + 1)
//    }

  def main(args: Array[String]): Unit = {
    //Example 01
    val hello = IO { println("Hello") }
    val world = IO { println("World") }
    (hello *> world).unsafeRunSync

    //Example 02
    val wait = IO { Thread.sleep(5000) }
    (hello *> wait *> world).unsafeRunSync
  }
}
