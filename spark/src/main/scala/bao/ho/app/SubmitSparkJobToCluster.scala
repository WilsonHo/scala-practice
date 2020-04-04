package bao.ho.app

import org.apache.spark.launcher.SparkLauncher

trait SubmitSparkJobToCluster {

  def test(arguments: Array[String]) = {
    println(arguments)
    //
    val javaHome  = "/Library/Java/JavaVirtualMachines/jdk1.8.0_72.jdk/Contents/Home";
    val sparkHome = "/Users/mparsian/spark-2.1.0";
    val appResource =
      "/Users/mparsian/zmp/github/data-algorithms-book/dist/data_algorithms_book.jar";
    val mainClass = "org.dataalgorithms.bonus.friendrecommendation.spark.SparkFriendRecommendation";
    //
    // parameters passed to the  SparkFriendRecommendation
    val appArgs = Array[String](
      //"--arg",
      "3",
      //"--arg",
      "/friends/input",
      //"--arg",
      "/friends/output"
    )
    //
    //
    val spark: SparkLauncher = new SparkLauncher()
      .setVerbose(true)
      .setJavaHome(javaHome)
      .setSparkHome(sparkHome)
      .setAppResource(appResource) // "/my/app.jar"
      .setMainClass(mainClass)     // "my.spark.app.Main"
      .setMaster("local")
      .setConf(SparkLauncher.DRIVER_MEMORY, "1g")
      .addAppArgs(appArgs: _*);
    //hosts:_*
    // Launches a sub-process that will start the configured Spark application.
    val proc: Process = spark.launch();
    //
    val inputStreamReaderRunnable: InputStreamReaderRunnable =
      new InputStreamReaderRunnable(proc.getInputStream(), "input");
    val inputThread: Thread = new Thread(inputStreamReaderRunnable, "LogStreamReader input");
    inputThread.start();
    //
    val errorStreamReaderRunnable: InputStreamReaderRunnable =
      new InputStreamReaderRunnable(proc.getErrorStream(), "error");
    val errorThread: Thread = new Thread(errorStreamReaderRunnable, "LogStreamReader error");
    errorThread.start();
    //
//    THE_LOGGER.info("Waiting for finish...");
    val exitCode: Int = proc.waitFor();
    println("Finished! Exit code:" + exitCode);
  }
}

object SubmitSparkJobToCluster extends SubmitSparkJobToCluster

object Example {

  def main(args: Array[String]): Unit =
    SubmitSparkJobToCluster.test(args)
}
