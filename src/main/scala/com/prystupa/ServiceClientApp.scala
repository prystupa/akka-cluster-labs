package com.prystupa

import akka.actor._
import akka.pattern.ask
import scala.concurrent.duration._
import akka.util.Timeout
import scala.util.Failure
import scala.util.Success
import org.slf4j.LoggerFactory

/**
 * Created with IntelliJ IDEA.
 * User: eprystupa
 * Date: 2/15/14
 * Time: 11:43 AM
 */
object ServiceClientApp extends App {

  val log = LoggerFactory.getLogger(ServiceClientApp.getClass)
  val system = ActorSystem("ClusterSystem")
  val serviceRegistry = system.actorSelection("akka.tcp://ClusterSystem@127.0.0.1:2551/user/serviceRegistry")

  implicit val timeout = Timeout(10.seconds)

  import system.dispatcher

  log.info("Locating serviceA...")
  serviceRegistry ask LocateServiceRequest("serviceA") onComplete {
    case Success(Some(service: ActorRef)) => {
      log.info("Found serviceA: {}", service)
      log.info("Sending request to serviceA: {}...", "ABC")
      service ask ServiceRequest("ABC") onComplete {
        case Success(reply) => {
          log.info("Received reply from serviceA: {}", reply)
          system shutdown()
        }
        case Failure(x) => log.error("Request to serviceA failed with: {}", x)
      }
    }
    case Success(None) => log.error("Could not locate serviceA")
    case Success(other) => log.error("Unexpected reply from service locator", other)
    case Failure(x) => log.error("Locating serviceA failed with: {}", x)
  }
}
