package com.prystupa

import akka.actor.{Actor, ActorLogging, Props, ActorSystem}

/**
 * Created with IntelliJ IDEA.
 * User: eprystupa
 * Date: 2/15/14
 * Time: 8:09 AM
 */

object ServiceProviderApp extends App {
  System.setProperty("akka.remote.netty.tcp.port", "2552")

  val system = ActorSystem("ClusterSystem")
  val serviceProvider = system.actorOf(Props[ServiceProviderActor], name = "serviceProvider")

}

class ServiceProviderActor extends Actor with ActorLogging {

  val serviceA = context.actorOf(Props[ServiceActor], name = "serviceA")
  val serviceB = context.actorOf(Props[ServiceActor], name = "serviceB")

  override def receive: Receive = {
    case DiscoverServicesRequest => {
      log.info("Received discover services request from {}", sender)
      log.info("Registering service A")
      sender ! ServiceRegistration(serviceA, "service A")
      log.info("Registering service B")
      sender ! ServiceRegistration(serviceB, "service B")
    }
    case other => log.warning("Received unknown message: {}", other)
  }
}

class ServiceActor extends Actor with ActorLogging {
  override def receive: Actor.Receive = {
    case other => log.warning("Unknown message received: {}", other)
  }
}