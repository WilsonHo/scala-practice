package bao.ho.app.JavaDatetime

import java.time.{Instant, LocalDate, ZoneOffset}

object InstantExample {

  def main(args: Array[String]): Unit = {
    val f = LocalDate.parse("2019-11-01")

    val t = LocalDate.parse("2019-11-01")

    val f1 = f.atStartOfDay(ZoneOffset.UTC).toInstant
    println(f1.getEpochSecond)

    val OneDaySeconds = 86400

    val t1 = t.atTime(23, 59, 59).toInstant(ZoneOffset.UTC)
    println(t1.getEpochSecond)

    val m1 = Range(f1.getEpochSecond.toInt, t1.getEpochSecond.toInt, OneDaySeconds)
      .map(p => Instant.ofEpochSecond(p.toLong))
      .toList
      .drop(1)
      .dropRight(1)

    println(m1)
  }
}
