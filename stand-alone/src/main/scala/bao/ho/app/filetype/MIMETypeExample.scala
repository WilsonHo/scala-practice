package bao.ho.app.filetype

import java.io.File

import net.sf.jmimemagic.Magic

object MIMETypeExample {

  def main(args: Array[String]): Unit = {
    val file = new File(
      "/Users/bao.ho/Working/Projects/flipflop_2019_01_04/rs-api-scalatra/target/scala-2.12/rs-api-scalatra_2.12-1.0.0.jar"
    )
    val matchfile = Magic.getMagicMatch(file, false)

    println(matchfile.getMimeType)
  }
}
