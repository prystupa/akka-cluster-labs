package com.prystupa

import akka.actor._
import akka.pattern.ask
import scala.concurrent.duration._
import akka.util.Timeout
import scala.util.Failure
import scala.util.Success

/**
 * Created with IntelliJ IDEA.
 * User: eprystupa
 * Date: 2/15/14
 * Time: 11:43 AM
 */
object ServiceClientApp extends App {

  val system = ActorSystem("ClusterSystem")

  val serviceRegistry = system.actorSelection("akka.tcp://ClusterSystem@127.0.0.1:2551/user/serviceRegistry")

  implicit val timeout = Timeout(10.seconds)

  import system.dispatcher

  println("Locating serviceA...")
  serviceRegistry ask LocateServiceRequest("serviceA") onComplete {
    case Success(Some(service: ActorRef)) => {
      println("    found serviceA: {}", service)
      println("Sending request to serviceA: {}...", "ABC")
      service ask ServiceRequest("ABC") onComplete {
        case Success(reply) => println("    received reply from serviceA: {}", reply)
        case Failure(x) => println("    request to serviceA failed with: {}", x)
      }
    }
    case Success(None) => println("    could not locate serviceA")
    case Success(other) => println("    unexpected reply from service locator", other)
    case Failure(x) => println("    locating serviceA failed with: {}", x)
  }
}
