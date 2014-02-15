package com.prystupa

import akka.actor._
import akka.cluster.ClusterEvent._
import akka.cluster.{MemberStatus, Member, Cluster}
import akka.cluster.ClusterEvent.CurrentClusterState
import akka.cluster.ClusterEvent.MemberUp

/**
 * Created with IntelliJ IDEA.
 * User: eprystupa
 * Date: 2/15/14
 * Time: 8:02 AM
 */
object ServiceRegistryApp extends App {
  System.setProperty("akka.remote.netty.tcp.port", "2551")

  val system = ActorSystem("ClusterSystem")
  val serviceRegistry = system.actorOf(Props[ServiceRegistryActor], name = "serviceRegistry")
  val cluster = Cluster(system)

  cluster registerOnMemberUp {
    cluster.subscribe(serviceRegistry, classOf[ClusterDomainEvent])
  }
}

class ServiceRegistryActor extends Actor with ActorLogging {

  var services: Map[String, ActorRef] = Map()

  override def receive: Receive = {

    case state: CurrentClusterState ⇒
      log.info("Joined cluster, current members are: {}", state.members.mkString(", "))
      state.members.filter(_.status == MemberStatus.Up).foreach(registerProvider)

    case MemberUp(member) ⇒
      log.info("Member is Up: {}", member.address)
      registerProvider(member)

    case _: ClusterDomainEvent ⇒ // ignore

    case msg@ServiceRegistration(service, metadata) => {
      log.info("Received service registration info: {}", msg)
      context.watch(service)
      services = services updated(metadata, service)
      log.info("Current registered services: {}", services.mkString(", "))
    }

    case LocateServiceRequest(service) => sender ! services.get(service)

    case Terminated(service) => {
      log.info("Notified of registered endpoint termination, un-registering: {}", service)
      services = services.filterNot(_ == service)
      log.info("Remaining registered services: {}", services.mkString(", "))
    }

    case other => log.warning("Unexpected message received: {}", other)
  }

  private def registerProvider(member: Member): Unit = {
    val serviceProvider = context.actorSelection(RootActorPath(member.address) / "user" / "serviceProvider")

    log.info("Sending services discovery request to {}", serviceProvider)
    serviceProvider ! DiscoverServicesRequest
  }
}