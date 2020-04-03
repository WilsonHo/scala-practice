package bao.ho.app

import org.apache.livy.{Job, JobContext}

class SparkExample extends Job[Int] {
  override def call(jobContext: JobContext): Int = ???
}
