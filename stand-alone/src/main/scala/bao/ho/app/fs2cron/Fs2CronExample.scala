package bao.ho.app.fs2cron

import java.io.{File, FileInputStream, FileOutputStream, InputStream, OutputStream}
import java.nio.file.{Files, Path, Paths}
import java.time.LocalTime

import cats.effect.concurrent.Semaphore
import cats.effect.{ExitCode, IO, IOApp, Resource, Sync}
import cron4s.Cron
import eu.timepit.fs2cron.awakeEveryCron
import fs2.Stream
import cats.syntax.flatMap._
import eu.timepit.fs2cron.schedule

object Fs2CronExample extends IOApp {

  object FirstExample {

    def exec = {
      val evenSeconds = Cron.unsafeParse("*/2 * * ? * *")

      val printTime = Stream.eval(IO(println(LocalTime.now)))

      val scheduledTasks = awakeEveryCron[IO](evenSeconds) >> printTime

      scheduledTasks.repeat.compile.drain >> IO.pure(ExitCode.Success)
    }
  }

  object SecondExample {

    def exec = {
      val evenSeconds      = Cron.unsafeParse("*/2 * * ? * *")
      val everyFiveSeconds = Cron.unsafeParse("*/5 * * ? * *")
      val scheduledTasks = schedule(
        List(
          evenSeconds      -> Stream.eval(IO(println(LocalTime.now.toString + " task 1"))),
          everyFiveSeconds -> Stream.eval(IO(println(LocalTime.now.toString + " task 2")))
        )
      )
      scheduledTasks.repeat.compile.drain >> IO.pure(ExitCode.Success)
    }
  }

  object ThirdExample {
    import cats.syntax.all._

    def inputStream[F[_]: Sync](f: File, guard: Semaphore[F]): Resource[F, FileInputStream] =
      Resource.make {
        Sync[F].delay(new FileInputStream(f))
      } { inStream =>
        guard.withPermit {
          Sync[F].delay(inStream.close()).handleErrorWith(_ => Sync[F].unit)
        }
      }

    def outputStream[F[_]: Sync](f: File, guard: Semaphore[F]): Resource[F, FileOutputStream] =
      Resource.make {
        Sync[F].delay(new FileOutputStream(f))
      } { outStream =>
        guard.withPermit {
          Sync[F].delay(outStream.close()).handleErrorWith(_ => Sync[F].unit)
        }
      }

    def inputOutputStreams[F[_]: Sync](
      in: File,
      out: File,
      guard: Semaphore[F]
    ) =
      Stream
        .resource(for {
          inStream  <- inputStream(in, guard)
          outStream <- outputStream(out, guard)
        } yield (inStream, outStream))

    def createResource(path: Path) = Resource.fromAutoCloseable(IO(Files.newBufferedReader(path)))

    def readFile(path: Path) =
      Stream
        .resource(createResource(path))

    def handle(inputPath: Path, outputPath: Path): Stream[IO, Unit] = ???
//    {
//      for{
//        (input, output) <- inputOutputStreams()
//      }
//    }

    def exec(args: List[String]): IO[ExitCode] = {
      val inputPath  = Paths.get(args.head)
      val outputPath = Paths.get(args(1))
      handle(inputPath, outputPath).compile.drain.as(ExitCode.Success)
    }
  }

  object FourExample extends FsExample[IO, Unit] {
    import cats.effect.{Blocker, IO}
    import fs2.{Stream, io, text}
    import java.nio.file.Paths
    import cats.implicits._

    def converter(inputPath: String, outputPath: String): Stream[IO, Unit] =
      Stream.resource(Blocker[IO]).flatMap { blocker =>
        def fahrenheitToCelsius(f: Double): Double =
          (f - 32.0) * (5.0 / 9.0)

        io.file
          .readAll[IO](Paths.get(inputPath), blocker, 4096)
          .through(text.utf8Decode)
          .through(text.lines)
          .filter(s => !s.trim.isEmpty && !s.startsWith("//"))
          .map(line => fahrenheitToCelsius(line.toDouble).toString)
          .intersperse("\n")
          .through(text.utf8Encode)
          .through(io.file.writeAll(Paths.get(outputPath), blocker))
      }

    def exec(args: List[String]): IO[ExitCode] =
      converter(args.head, args(1)).compile.drain.as(ExitCode.Success)

    override def worker(args: List[String]): Stream[IO, Unit] = ???
  }

  object FifthExample extends FsExample[IO, Unit] {
    import cats.implicits._

    def putStrLn[A](a: A): IO[Unit] = IO(println(a))
    def src                         = Stream.range[IO](1, 11)

    def exec(args: List[String]): IO[ExitCode] = {
      println(args)
      src.evalMap(putStrLn).compile.drain.as(ExitCode.Success)
    }

    override def worker(args: List[String]): Stream[IO, Unit] = ???
  }

  override def run(args: List[String]): IO[ExitCode] =
    FifthExample.exec(args)
}
