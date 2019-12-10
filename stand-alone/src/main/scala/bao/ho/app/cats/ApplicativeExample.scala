package bao.ho.app.cats
import cats._
import cats.data._
import cats.implicits._

object ApplicativeExample {

  trait UserValidator[F[_]] {
    def createValidUser(name: String, age: Int, email: String): F[User]
  }

  object UserValidator {
    def apply[F[_]](implicit ev: UserValidator[F]): UserValidator[F] = ev

    def validate[F[_]: UserValidator, E](name: String, age: Int, email: String): F[User] =
      UserValidator[F].createValidUser(name, age, email)
  }

  val userValidatorIdInterpreter = new UserValidator[Id] {

    def createValidUser(name: String, age: Int, email: String): Id[User] =
      User(name, age, email)
  }

  final case class User(
    name: String,
    age: Int,
    email: String
  )

  sealed trait UserValidationError
  // defined trait UserValidationError
  case object NameNotValid extends UserValidationError
  // defined object NameNotValid
  case object AgeOutOfRange extends UserValidationError
  // defined object AgeOutOfRange
  case object EmailNotValid extends UserValidationError
  // defined object EmailNotValid

  def main(args: Array[String]): Unit = {
    implicit val userValidatorInterpreter = userValidatorIdInterpreter
    // userValidatorInterpreter: UserValidator[cats.Id] = $anon$1@49cfb62b

    println(UserValidator.validate("John", 25, "john@example.com"))
    // User(John,25,john@example.com)

    println(UserValidator.validate("John", 25, "johnn@example"))
    // User(John,25,johnn@example)

    println(UserValidator.validate("John", -1, "john@gexample"))
    // User(John,-1,john@gexample)

    println(UserValidator.validate(".John", -1, "john@gexample"))
  }
}
