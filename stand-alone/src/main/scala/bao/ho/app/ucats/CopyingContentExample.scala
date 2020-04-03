package bao.ho.app.ucats

import cats.effect.{Concurrent, ExitCode, IO, IOApp, Resource, Sync}
import java.io.{File, FileInputStream, FileOutputStream, InputStream, OutputStream}

import cats.effect.concurrent.Semaphore

object CopyingContentExample extends IOApp {
//  def inputStream(f: File): Resource[IO, FileInputStream] =
//    Resource.fromAutoCloseable(IO(new FileInputStream(f)))

//  def inputStream(f: File): Resource[IO, FileInputStream] =
//    Resource.make {
//      IO(new FileInputStream(f)) // build
//    } { inStream =>
//      IO(inStream.close()).handleErrorWith(_ => IO.unit) // release
//    }
//
//  def outputStream(f: File): Resource[IO, FileOutputStream] =
//    Resource.make {
//      IO(new FileOutputStream(f)) // build
//    } { outStream =>
//      IO(outStream.close()).handleErrorWith(_ => IO.unit) // release
//    }
//
//  def inputOutputStreams(in: File, out: File): Resource[IO, (InputStream, OutputStream)] =
//    for {
//      inStream  <- inputStream(in)
//      outStream <- outputStream(out)
//    } yield (inStream, outStream)

  def inputStream[F[_]: Sync](f: File, guard: Semaphore[F]): Resource[F, FileInputStream] = {
    import cats.syntax.applicativeError._
    Resource.make {
      Sync[F].delay(new FileInputStream(f))
    } { inStream =>
      guard.withPermit {
        Sync[F].delay(inStream.close()).handleErrorWith(_ => Sync[F].unit)
      }
    }
  }

  def outputStream[F[_]: Sync](f: File, guard: Semaphore[F]): Resource[F, FileOutputStream] = {
    import cats.syntax.applicativeError._
    Resource.make {
      Sync[F].delay(new FileOutputStream(f))
    } { outStream =>
      guard.withPermit {
        Sync[F].delay(outStream.close()).handleErrorWith(_ => Sync[F].unit)
      }
    }
  }

  def inputOutputStreams[F[_]: Sync](
    in: File,
    out: File,
    guard: Semaphore[F]
  ): Resource[F, (InputStream, OutputStream)] =
    for {
      inStream  <- inputStream(in, guard)
      outStream <- outputStream(out, guard)
    } yield (inStream, outStream)

  def copy[F[_]: Concurrent](origin: File, destination: File): F[Long] = {
    import cats.syntax.flatMap._
    import cats.syntax.functor._
    for {
      guard <- Semaphore[F](1)
      count <- inputOutputStreams(origin, destination, guard).use {
                case (in, out) =>
                  guard.withPermit(transfer(in, out))
              }
    } yield count
  }

  def transfer[F[_]: Sync](origin: InputStream, destination: OutputStream): F[Long] = {
    import cats.syntax.flatMap._
    import cats.syntax.functor._
    for {
      buffer <- Sync[F].delay(new Array[Byte](1024 * 10)) // Allocated only when the IO is evaluated
      total  <- transmit(origin, destination, buffer, 0L)
    } yield total
  }

  def transmit[F[_]: Sync](
    origin: InputStream,
    destination: OutputStream,
    buffer: Array[Byte],
    acc: Long
  ): F[Long] = {
    import cats.syntax.flatMap._
    import cats.syntax.functor._
    for {
      amount <- Sync[F].delay(origin.read(buffer, 0, buffer.size))
      count <- if (amount > -1)
                Sync[F].delay(destination.write(buffer, 0, amount)) >> transmit(
                  origin,
                  destination,
                  buffer,
                  acc + amount
                )
              else
                Sync[F].pure(acc) // End of read stream reached (by java.io.InputStream contract), nothing to write
    } yield count                 // Returns the actual amount of bytes transmitted
  }

//  def copy(origin: File, destination: File): IO[Long] =
//    inputOutputStreams(origin, destination).use {
//      case (in, out) =>
//        transfer(in, out)
//    }

//  def copy(origin: File, destination: File): IO[Long] = {
//    val inIO: IO[InputStream]  = IO(new FileInputStream(origin))
//    val outIO:IO[OutputStream] = IO(new FileOutputStream(destination))
//
//    (inIO, outIO)              // Stage 1: Getting resources
//      .tupled                  // From (IO[InputStream], IO[OutputStream]) to IO[(InputStream, OutputStream)]
//      .bracket{
//        case (in, out) =>
//          transfer(in, out)    // Stage 2: Using resources (for copying data, in this case)
//      } {
//        case (in, out) =>      // Stage 3: Freeing resources
//          (IO(in.close()), IO(out.close()))
//            .tupled              // From (IO[Unit], IO[Unit]) to IO[(Unit, Unit)]
//            .handleErrorWith(_ => IO.unit).void
//      }
//  }
  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- if (args.length < 2)
            IO.raiseError(new IllegalArgumentException("Need origin and destination files"))
          else IO.unit
      orig  = new File(args(0))
      dest  = new File(args(1))
      count <- copy[IO](orig, dest)
      _     <- IO(println(s"$count bytes copied from ${orig.getPath} to ${dest.getPath}"))
    } yield ExitCode.Success
}
