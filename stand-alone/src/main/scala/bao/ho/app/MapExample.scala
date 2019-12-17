package bao.ho.app

object MapExample {
  sealed trait A
  case object B extends A
  case object C extends A
  case object D extends A

  val m = Map("a" -> B, "b" -> C)

  def main(args: Array[String]): Unit = {
    println(m.mapValues {
      case C | B => """println("ok")"""
      case _     => """println("NOT")"""
    })

    val l = m.values.filter {
      case B | C => true
      case _     => false
    }
    println(l)

    val k = m.values.foldLeft(true) {
      case (b, op) =>
        op match {
          case B | C => b
          case _     => false
        }
    }

    println(k)
  }
}
