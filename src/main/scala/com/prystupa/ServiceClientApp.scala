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

  serviceRegistry ask LocateServiceRequest("serviceA") onComplete {
    case Success(Some(service: ActorRef)) => {
      println("received service for serviceA: {}", service)
      service ask ServiceRequest("ABC") onSuccess {
        case reply => println("Received reply from serviceA: {}", reply)
      }
    }
    case Success(other) => println("unexpected reply from service locator", other)
    case Failure(x) => println("request to serviceA failed with: {}", x)
  }
}
