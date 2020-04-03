package bao.ho.app.fs2cron

import java.net.InetSocketAddress

import cats.effect.{ExitCode, IO, IOApp, Sync}
import cats.effect.concurrent.Deferred
import fs2.{Chunk, Stream}
import fs2.io.tcp.SocketGroup

object Fs2IOExample extends IOApp {

  object TcpExample extends FsExample[IO, Unit] {
    import cats.effect._
    import cats.syntax.flatMap._
    import scala.concurrent.duration._

    def client[F[_]: Concurrent: ContextShift]: F[Unit] =
      Blocker[F].use { blocker =>
        SocketGroup[F](blocker).use { socketGroup =>
          socketGroup.client(new InetSocketAddress("localhost", 5555)).use { socket =>
            socket.write(Chunk.bytes("Hello, world!".getBytes)) >>
              socket.read(8192).flatMap { response =>
                Sync[F].delay(println(s"Response: $response"))
              }
          }
        }
      }
    override def worker(args: List[String]): Stream[IO, Unit] = ???
  }

  object Second extends FsExample[IO, Unit] {
    import cats.effect._
    import scala.concurrent.duration._

    override def worker(args: List[String]): Stream[IO, Unit] = ???
  }

  object Third extends FsExample[IO, Unit] {
    import cats.effect._
    import scala.concurrent.duration._

    override def worker(args: List[String]): Stream[IO, Unit] = ???
  }
  override def run(args: List[String]): IO[ExitCode] = Third.exec(args)
}
