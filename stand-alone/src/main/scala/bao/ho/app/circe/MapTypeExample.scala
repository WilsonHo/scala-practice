package bao.ho.app.circe

import java.time.LocalDate
import java.time.ZoneOffset.UTC

import bao.ho.app.circe.MapTypeExample.ReconProcessingState.{
  Holiday,
  JobSubmitted,
  MissingInputs,
  MissingTemporalDependencies,
  ReadyForProcessing
}
import io.circe.{Encoder, Json}
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredEncoder

object MapTypeExample {
  implicit val configuration = Configuration.default.withSnakeCaseMemberNames

  sealed trait ReconProcessingState

  object ReconProcessingState {
    final case class ReadyForProcessing(sourceResponses: String) extends ReconProcessingState

    case object ReadyForProcessing {
      implicit val encoder: Encoder[ReadyForProcessing] = deriveConfiguredEncoder
    }

    final case class MissingInputs(inputs: List[String]) extends ReconProcessingState

    object MissingInputs {
      implicit val encoder: Encoder[MissingInputs] = deriveConfiguredEncoder
    }
    final case class JobSubmitted(jobId: String) extends ReconProcessingState

    case object JobSubmitted {
      implicit val encoder: Encoder[JobSubmitted] = deriveConfiguredEncoder
    }

    final case class MissingTemporalDependencies(inputs: List[String], output: Boolean)
        extends ReconProcessingState

    case object MissingTemporalDependencies {
      implicit val encoder: Encoder[MissingTemporalDependencies] = deriveConfiguredEncoder
    }

    case object Holiday extends ReconProcessingState
  }

  def main(args: Array[String]): Unit = {
    import io.circe.syntax._
    type Response = Map[LocalDate, ReconProcessingState]

    implicit val encoder: Encoder[Response] = (result: Response) => {
      import io.circe.syntax._

      def f(pair: (LocalDate, Json)): (String, Json) =
        (pair._1.atStartOfDay(UTC).toEpochSecond.toString, pair._2)

      case class Result(status: String, details: Option[Json])
      object Result {
        implicit val encoder: Encoder[Result] = (result: Result) => {
          val status = Json.obj(
            ("status", Configuration.snakeCaseTransformation(result.status).asJson)
          )
          val detail = result.details.fold(Json.Null)(d => Json.obj(("details", d)))
          detail.deepMerge(status)
        }
      }

      import cats.syntax.option._
      val fields: Seq[(String, Json)] = result
        .mapValues {
          case _ @ReadyForProcessing(a) =>
            Result(a.toString, a.asJson.some).asJson
          case obj: MissingInputs => Result(MissingInputs.toString, obj.asJson.some).asJson
          case obj: JobSubmitted  => Result(JobSubmitted.toString, obj.asJson.some).asJson
          case obj: MissingTemporalDependencies =>
            Result(MissingTemporalDependencies.toString, obj.asJson.some).asJson
          case Holiday => Result(Holiday.toString, None).asJson
        }
        .map(f)
        .toList
      Json.obj(fields: _*)
    }

    val d1 = LocalDate.parse("2019-12-16")
    val d2 = LocalDate.parse("2019-12-17")
    val d3 = LocalDate.parse("2019-12-18")
    val demo: Map[LocalDate, ReconProcessingState] =
      Map(d1 -> Holiday, d2 -> ReadyForProcessing("Demo"), d3 -> JobSubmitted("100"))
    println(demo.asJson)
  }
}
