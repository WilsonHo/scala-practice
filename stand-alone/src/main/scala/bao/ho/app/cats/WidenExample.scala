package bao.ho.app.cats

import cats.effect.IO

object WidenExample {
  import cats.syntax.functor._

  abstract class A() {
    def say() = println("Hello A")
  }

  case class B() extends A {
    override def say() = println("Hello B")
  }
  case class C() extends A {}

  import cats.implicits._

  def main(args: Array[String]): Unit = {
//    val b: IO[B] = IO.delay(B())
//    val a: IO[A] = b.widen[A]
//    val c: IO[C] = a.narrow[C]
//    b.map(_.say()).unsafeRunSync()
//    a.map(_.say()).unsafeRunSync()
//    c.map(_.say()).unsafeRunSync()
  }
}
