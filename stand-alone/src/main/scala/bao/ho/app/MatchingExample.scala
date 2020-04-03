package bao.ho.app

object MatchingExample {

  def main(args: Array[String]): Unit = {
    val a = List(1, 2, 3)
    a match {
      case (1 :: 2 :: _)        => println(s"Bye")
      case (1 :: _ :: _ :: Nil) => println(s"Hello")
      case _                    => println("None")
    }
  }
}
