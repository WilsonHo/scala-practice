package bao.ho.app.circe

import io.circe.{Decoder, Encoder}
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.{deriveConfiguredDecoder, deriveConfiguredEncoder}

object ValueClassExample {
  import io.circe.parser.decode

  case class Number(a: Int) extends AnyVal

  object Number {
    implicit val configuration            = Configuration.default.withSnakeCaseMemberNames
    implicit val decoder: Decoder[Number] = Decoder.decodeInt.map(Number.apply)
    implicit val encoder: Encoder[Number] = Encoder.encodeInt.contramap(_.a)
  }

  case class Demo(num: Number)

  object Demo {
    implicit val configuration = Configuration.default.withSnakeCaseMemberNames
    implicit val decoder       = deriveConfiguredDecoder[Demo]
    implicit val encoder       = deriveConfiguredEncoder[Demo]
  }

  def main(args: Array[String]): Unit = {
    import Demo._
    import io.circe.syntax._
    val d = Demo(Number(100))

    println(d.asJson)
    val m = """{"num":100}"""
    println(decode[Demo](m))

//
  }
}
