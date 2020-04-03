package bao.ho.app

import monocle.Iso

import scala.util.Try

object MonocleExample {

//  object ExampleOne {
//    import monocle.Lens
//    import monocle.macros.GenLens
//    import monocle.function.Cons.headOption
//
//    case class Street(number: Int, name: String)
//    case class Address(city: String, street: Street)
//    case class Company(name: String, address: Address)
//    case class Employee(name: String, company: Company)
//
//    def run() = {
//      val employee =
//        Employee("john", Company("awesome inc", Address("london", Street(23, "high street"))))
//      val company: Lens[Employee, Company] = GenLens[Employee](_.company)
//      val address: Lens[Company, Address]  = GenLens[Company](_.address)
//      val street: Lens[Address, Street]    = GenLens[Address](_.street)
//      val streetName: Lens[Street, String] = GenLens[Street](_.name)
//
//      company
//        .composeLens(address)
//        .composeLens(street)
//        .composeLens(streetName)
//        .modify(_.capitalize)(employee)
//
//      company
//        .composeLens(address)
//        .composeLens(street)
//        .composeLens(streetName)
//        .composeOptional(headOption)
//        .modify(_.toUpper)(employee)
//    }
//  }
//
//  object ExampleSecond {
//    import monocle.Lens
//    import monocle.macros.GenLens
//
//    case class Street(name: String, number: Int)
//
//    def run() = {
//      val nameLens1: Lens[Street, String] = Lens
//        .apply[Street, String](street => street.name)(
//          newName => street => street.copy(name = newName)
//        )
//
//      val nameLens2 = GenLens[Street](_.name)
//      val street    = Street("Abbey Road", 44)
//      println(nameLens1.get(street))
//      println(nameLens2.get(street))
//
//      street
//        .copy(name = "Newton Street copy")
//
//      nameLens1.set("Newton Street1")(street)
//      nameLens2.set("Newton Street2")(street)
//
//      street
//        .copy(name = street.name.toUpperCase)
//
//      nameLens1.modify(_.toUpperCase)(street)
//      nameLens2.modify(_.toUpperCase)(street)
//    }
//  }
//
  object ExampleThird {
    import monocle.Prism

    sealed trait Json
    case object JNull                     extends Json
    case class JStr(v: String)            extends Json
    case class JNum(v: Double)            extends Json
    case class JObj(v: Map[String, Json]) extends Json

    sealed trait Conf
    case object Error                           extends Conf
    case class DataConf(id: Long, uuid: String) extends Conf

    case class Percent private (value: Int) {
      require(value >= 0)
      require(value <= 100)
    }

    object Percent {

      def fromInt(input: Int): Option[Percent] =
        if (input >= 0 && input <= 100) {
          Some(Percent(input))
        } else {
          None
        }
      val intToPercentPrism = Prism[Int, Percent](i => Percent.fromInt(i))(_.value)
    }

    case class Meter(whole: Int, fraction: Int)
    case class Centimeter(whole: Int)

    def run() = {
//      val stringPrism = Prism[Json, String] {
//        case JStr(v) => Some(v)
//        case _       => None
//      }(str => JStr(str))

//      val stringPrism1 = Prism.partial[Json, String] { case JStr(v) => v }(JStr)

      val uuidPrism = Prism[Conf, String] {
        case DataConf(_, uuid) => Some(uuid)
        case _                 => None
      }(str => DataConf(0, str))

      val m = uuidPrism.modifyOption(_.toLowerCase)(Error)
      println(m)
      val stringToIntPrism = Prism[String, Int](str => Try(str.toInt).toOption)(_.toString)
      println(stringToIntPrism.getOption("100"))
      println(stringToIntPrism.modify(_ + 1)("100"))
      println(stringToIntPrism.modify(_ + 1)("ab"))

      val centimeterToMeterIso = Iso[Centimeter, Meter] { cm =>
        Meter(cm.whole / 100, cm.whole % 100)
      } { m =>
        Centimeter(m.whole * 100 + m.fraction)
      }

      println(centimeterToMeterIso.modify(m => m.copy(m.whole + 3))(Centimeter(155)))
//      println(stringPrism.getOption(JStr("someString")))
//      println(stringPrism.reverseGet("someString"))
//      println(stringPrism.getOption(JNull))
    }
  }

  object UniversityExample {
    import monocle.macros.GenLens
    import monocle.function.At.at // to get at Lens
    import monocle.std.map._      // to get Map instance for At
//    import ucats
//    import cats._
//    import cats.data._
    import cats.syntax.option._

    case class Lecturer(firstName: String, lastName: String, salary: Int)
    case class Department(budget: Int, lecturers: List[Lecturer])
    case class University(name: String, departments: Map[String, Department])

    val uni = University(
      "oxford",
      Map(
        "Computer Science" -> Department(
          45,
          List(
            Lecturer("john", "doe", 10),
            Lecturer("robert", "johnson", 16)
          )
        ),
        "History" -> Department(
          30,
          List(
            Lecturer("arnold", "stones", 20)
          )
        )
      )
    )

    def run() = {
      val departments = GenLens[University](_.departments)
      departments.composeLens(at("History")).set(None)(uni)

      val physics = Department(
        36,
        List(
          Lecturer("daniel", "jones", 12),
          Lecturer("roger", "smith", 14)
        )
      )
      departments.composeLens(at("Physics")).set(physics.some)(uni)
      println(physics)
    }
  }

  def main(args: Array[String]): Unit =
    UniversityExample.run()
//    val stringToList = Iso[String, List[Char]](_.toList)(_.mkString(""))
//
//    println(stringToList.modify(_.tail)("Hello"))
}
