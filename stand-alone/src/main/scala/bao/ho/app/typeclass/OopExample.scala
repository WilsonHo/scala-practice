package bao.ho.app.typeclass

object OopExample {

  sealed trait Person {
    def print(): Unit
    def work(job: String): String
  }

  object Person {

    final case class Men(name: String) extends Person {
      override def print(): Unit = println(s"I'm men with name ${name}")

      override def work(job: String): String = s"I am working ${job} full day"
    }

    final case class Women(name: String) extends Person {
      override def print(): Unit = println(s"I'm women with name ${name}")

      override def work(job: String): String = s"I am NOT working ${job}"
    }
  }

  def main(args: Array[String]): Unit = {
    val p3 = Person.Men("Bao")
    val p1 = Person.Women("Nuong")
    p3.print()
    p1.print()
  }
}
