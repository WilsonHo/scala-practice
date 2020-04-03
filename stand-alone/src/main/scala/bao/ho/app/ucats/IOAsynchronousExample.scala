package bao.ho.app.ucats

import cats.effect.{ExitCode, IO, IOApp}

object IOAsynchronousExample {
//  def k1: String => String = _ + "1000"
//  def k2: String => String = _ + "2000"
//
//  def exec(m: String, f: String => String) = f(m)
//
//  def run: Unit = {
//    val a = true
//    if (a) exec("Bao", k1)
//    else exec("Name", k2)
//  }
//
//  def f(l: String => String): String = {
//    val m = "M3"
//    l(m)
//  }
//
//  f { a =>
//    (a + "1000")
//  }
//
//  def f1(l: (String => Int) => String): String =
//    l.apply(m)
//
//  def o: String => Double = _.length.toDouble
//
//  def g1(a: (String => Double) => Int): Int =
//    a.apply(o)
//
//  def g5 = (a: String => Double) => a.apply("Bao").toInt
//
//  def g2(a: String => Double => Int): Boolean = ???
//
//  def g3 = (a: String) => a.length
//
//  def g4: String => Double => Int = (a: String) => (l: Double) => (a.length + l).toInt
////  def g5: (String => Double) => Int = (a: String) => a.length.toDouble
//
//  def main(args: Array[String]): Unit = {
//    IO.async[String] { e =>
//      e.apply()
//    }
//    ////      .flatMap(_ => IO(ExitCode.Success))
//    println(g1 { g5 })
//  }
//
//  def l = {
//    g1 { a =>
//      a.apply("Bao").toInt
//    }
//
//    g2 { g4 }
//  }
//
//  def m: String => Int             = _.length
//  def k: (String => Int) => String = x => x("abc").toString
//
////  def main(args: Array[String]): Unit =
////    println(f1(k))
//
//  def fa(e: Either[Throwable, String]): Unit = println("Hello " + e)
//
//  def fb(f: Either[Throwable, String] => Unit): Unit = {
//    val v = Either.cond(1 == 0, "100", new RuntimeException("RuntimeException"))
//
//    f.apply(v)
//  }
//
////  val lh = IO.delay {
////    println("Hello")
////    println("Hello")
////    println("Hello")
////    println("Hello")
////    println("Hello")
////    println("Hello")
////    println("Hello")
////    println("Hello")
////  }
////
////  import ucats.~>
////  import ucats.implicits._
////  import ucats.syntax.applicative._
////  import ucats.syntax.apply._
////  import ucats.syntax.functor._
////  import ucats.syntax.flatMap._
////  import ucats.syntax.flatMap._
//
////  override def run(args: List[String]): IO[ExitCode] =
//////
////    IO.race(IO.never, lh).flatMap(_ => IO(ExitCode.Success)) //<-> lh.map(Left(_))
}
