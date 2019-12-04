package bao.ho.app

import java.io.{DataInputStream, File, FileInputStream}

import org.scalatra._

import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future}

class DownloadController extends ScalatraServlet with FutureSupport {
  // /download?file_name=abc.csv
  get("/") {
    new AsyncResult {
      import scala.concurrent.duration._
      override def timeout: Duration = Duration.Inf
      val is =
        Future {
          val optFileName = params.get("file_name")
          val file = new File(
            "/tmp/datasets/etl_output/GFX_FOBO_CA_Spot_AEJ/2019/11/14/outputs/application_1573654458283_0192/adjustment_template.csv"
          )
          val inputStream = new DataInputStream(new FileInputStream(file))
          //    val result = IOUtils.toByteArray(inputStream)
          val downloadHeader = Map(
            "Content-Type" -> ("application/octet-stream"),
            "Content-Disposition" -> (s"""attachment; filename="${optFileName
              .getOrElse("download_file")}"""")
          )
          Ok(inputStream, downloadHeader)
        }
    }
  }

  override protected implicit def executor: ExecutionContext = ExecutionContext.global
}
