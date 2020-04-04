package bao.ho.app

import java.util.logging.Logger

import org.apache.spark.launcher.SparkLauncher

object SubmitSparkPiToCluster {
  val THE_LOGGER: Logger = Logger.getLogger(getClass.getName)

  def main(args: Array[String]): Unit = {
    val startTime = System.currentTimeMillis();
    submit(args); // ... the code being measured ...
    val estimatedTime = System.currentTimeMillis() - startTime;
    THE_LOGGER.info("estimatedTime (millis)=" + estimatedTime);
  }

  def submit(args: Array[String]) = {
    println(args)
    //
    val javaHome: String  = "/Library/Java/JavaVirtualMachines/jdk1.8.0_192.jdk/Contents/Home"
    val sparkHome: String = "/Users/bao.ho/Downloads/spark-1.6.2-bin-hadoop2.6"
    val appResource: String =
      "/Users/bao.ho/Downloads/spark-1.6.2-bin-hadoop2.6/lib/spark-examples-1.6.2-hadoop2.6.0.jar"
    val mainClass: String = "org.apache.spark.examples.SparkPi"
    //
    // parameters passed to the  SparkPi
    val appArgs: Array[String] = Array("100")
    val spark: SparkLauncher = new SparkLauncher()
      .setVerbose(true)
      .setJavaHome(javaHome)
      .setSparkHome(sparkHome)
      .setAppResource(appResource) // "/my/app.jar"
      .setMainClass(mainClass)     // "my.spark.app.Main"
      //      .setMaster("spark://bao-spark-master:7077")
      .setMaster("local[*]")
      .setConf(SparkLauncher.DRIVER_MEMORY, "2g")
      .addAppArgs(appArgs: _*)
    //
    // Launches a sub-process that will start the configured Spark application.
    val proc: Process = spark.launch();
    //
    val inputStreamReaderRunnable: InputStreamReaderRunnable =
      new InputStreamReaderRunnable(proc.getInputStream(), "input");
    val inputThread = new Thread(inputStreamReaderRunnable, "LogStreamReader input");
    inputThread.start();
    //
    val errorStreamReaderRunnable: InputStreamReaderRunnable =
      new InputStreamReaderRunnable(proc.getErrorStream(), "error");
    val errorThread: Thread = new Thread(errorStreamReaderRunnable, "LogStreamReader error");
    errorThread.start();
    //
    THE_LOGGER.info("Waiting for finish...");
    val exitCode = proc.waitFor();
    THE_LOGGER.info("Finished! Exit code:" + exitCode);
  }
}
