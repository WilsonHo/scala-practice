package bao.ho.app

import java.io.{DataInputStream, File, FileInputStream, InputStream}

import org.apache.commons.io.IOUtils
import org.scalatra._
import org.scalatra.servlet.FileUploadSupport

class DownloadController extends ScalatraServlet with FileUploadSupport with FlashMapSupport {
  // /download?file_name=abc.csv
  get("/") {
    val optFileName = params.get("file_name")
    val file        = new File("/Users/bao.ho/Downloads/star2002-full.csv")
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
