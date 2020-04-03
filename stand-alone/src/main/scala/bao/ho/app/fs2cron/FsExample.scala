package bao.ho.app.fs2cron

import cats.effect.{ExitCode, Sync}
import fs2.Stream

trait FsExample[F[_], A] {
  import cats.syntax.flatMap._

  def worker(args: List[String]): Stream[F, A]

  def exec(args: List[String])(implicit F: Sync[F]): F[ExitCode] =
    worker(args).compile.drain >> F.delay(ExitCode.Success)
}
