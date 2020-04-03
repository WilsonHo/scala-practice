package bao.ho.app

import java.net.URL

import akka.actor.{Actor, ActorSystem, Props}
import java.net.URL

import scala.collection.JavaConverters._
import org.jsoup.nodes._
import org.jsoup._
import akka.actor._
import akka.pattern._
import akka.util.Timeout

import scala.concurrent.duration.SECONDS
import scala.concurrent.ExecutionContext.Implicits.global
import Protocol._

import scala.util.{Failure, Success, Try}

object ParallelWebCrawlerExample {

  def fetch(url: URL): Option[Set[URL]] =
    Try {
      Jsoup
        .connect(url.toString)
        .get
        .getElementsByAttribute("href")
        .asScala
        .map(h => new URL(url, h.attr("href")))
        .toSet
    }.toOption

  def fetchToDepth(url: URL, depth: Int, visited: Set[URL] = Set()): Set[URL] = {
    val links = fetch(url).getOrElse(Set())

    if (depth > 0)
      links ++ links
        .filter(!visited(_))
        .toList
        .zipWithIndex
        .foldLeft(Set[URL]()) {
          case (accum, (next, id)) =>
            println(s"Progress for depth $depth: $id of ${links.size}")
            accum ++ (if (!accum(next)) fetchToDepth(next, depth - 1, accum) else Set())
        }
        .toSet
    else links
  }

  def main(args: Array[String]): Unit = {
    val target = new URL("http://mvnrepository.com/")

    val res = fetchToDepth(target, 1)
    println(res.take(10).mkString("\n"))
    println(res.size)
  }
}

object ActorBased {
  implicit val timeout: Timeout = Timeout(30, SECONDS)

//  def main(args: Array[String]): Unit = {
//    val system = ActorSystem("PiSystem")
//    val root   = system.actorOf(Worker.workerProps)
//
//    (root ? Job(new URL("http://mvnrepository.com/"), 1)).onComplete {
//      case Success(value) =>
//        value ma
//      case Result(res) =>
//        println("Crawling finished successfully")
//        println(res.take(10).mkString("\n"))
//        println(res.size)
//      case Failure(exception) => ???
//    }
//  }
}

class Worker extends Actor {
  var buffer: Set[URL]          = Set()
  var children: Set[ActorRef]   = Set()
  var replyTo: Option[ActorRef] = None

  var answered = 0

  def receive = awaitingForTasks

  def dispatch(lnk: URL, depth: Int, visited: Set[URL]): Unit = {
    val child = context.actorOf(Worker.workerProps)
    children += child
    child ! Job(lnk, depth, visited)
  }

  def awaitingForTasks: Receive = {
    case Job(url, depth, visited) =>
      replyTo = Some(sender)

      val links = ParallelWebCrawlerExample.fetch(url).getOrElse(Set()).filter(!visited(_))
      buffer = links

      if (depth > 0) {
        println(s"Processing links of $url, descending now")

        children = Set()
        answered = 0

        for { l <- links } dispatch(l, depth - 1, visited)
        context.become(processing)
      } else {
        println(s"Reached maximal depth on $url - returning its links only")
        sender ! Result(buffer)
        context.stop(self)
      }
  }

  def processing: Receive = {
    case Result(urls) =>
      replyTo match {
        case Some(to) =>
          answered += 1
          println(s"$self: $answered actors responded of ${children.size}")
          buffer ++= urls
          if (answered == children.size) {
            to ! Result(buffer)
            context.stop(self)
          }

        case None => println("replyTo actor is None, something went wrong")
      }
  }
}

object Worker {
  def workerProps: Props = Props(classOf[Worker])
}

object Protocol {
  sealed trait Msg
  case class Job(url: URL, depth: Int, visited: Set[URL] = Set()) extends Msg
  case class Result(urls: Set[URL])                               extends Msg
}
