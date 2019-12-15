package example

import akka.stream._
import akka.stream.scaladsl._
import akka.actor.ActorSystem

object Hello extends Greeting with App {
  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()

  val source = Source[Int](1 to 5)
  val sink = Sink.foreach[Int](println)

  source
   .map(_ * 2)
   .runWith(sink)

  println(greeting)
}

trait Greeting {
  lazy val greeting: String = "hello"
}
