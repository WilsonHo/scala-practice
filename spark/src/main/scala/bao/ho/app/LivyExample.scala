package bao.ho.app

import java.io.File
import java.net.URI

import org.apache.livy.scalaapi.LivyScalaClient
import org.apache.livy.{LivyClient, LivyClientBuilder}
//import com.cloudera.livy.scalaapi._

class LivyExample {

  def main(args: Array[String]): Unit = {
    val livyUrl = "http://192.168.56.2:8998"
//    val client: LivyClient = new LivyClientBuilder()
//      .setURI(new URI(livyUrl))
//      .build()
//
//    client

    val scalaClient = new LivyClientBuilder(false)
      .setURI(new URI(livyUrl))
      .build()
      .asInstanceOf[LivyScalaClient]

    scalaClient.uploadJar(new File("/user/hduser/jars/spark-examples_2.11-2.4.5.jar"))
    println("")
//    scalaClient.submit(new PiJob(samples)).get();
  }
}
