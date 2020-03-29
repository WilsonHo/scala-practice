package bao.ho.app.typeclass

object TypeClassExample {
  sealed trait Person

  object Person {
    final case class Men(name: String)   extends Person
    final case class Women(name: String) extends Person
  }

  trait Printer[A] {
    def print(value: A): Unit
  }

  object Printer {
    def apply[A](implicit A: Printer[A]): Printer[A] = A
  }

  trait Work[A] {
    def work(job: String): String
  }

  object Work {
    def apply[A](implicit W: Work[A]): Work[A] = W
  }

  implicit class WorkOps[A](value: A)(implicit W: Work[A]) {
    def work(job: String): String = W.work(job)
  }

  implicit class PrintOps[A](value: A)(implicit P: Printer[A]) {
    def print() = P.print(value)
  }

  def main(args: Array[String]): Unit = {
    implicit val menPrinter = new Printer[Person.Men] {
      override def print(value: Person.Men): Unit = println(s"I'm men with name ${value.name}")
    }

    implicit val menWork = new Work[Person.Men] {
      override def work(job: String): String = s"I am working ${job} full day"
    }

    implicit val womenPrinter = new Printer[Person.Women] {
      override def print(value: Person.Women): Unit = println(s"I'm women with name ${value.name}")
    }

    val p3 = Person.Men("Bao")
    val p1 = Person.Women("Nuong")
    p3.print()
    p1.print()

    print(p3.work("Hello"))
  }
}
