package com.prystupa

import akka.actor.{ActorLogging, Actor, Props, ActorSystem}
import akka.cluster.ClusterEvent._
import akka.cluster.ClusterEvent.MemberUp
import akka.cluster.ClusterEvent.UnreachableMember
import akka.cluster.ClusterEvent.MemberRemoved
import akka.cluster.Cluster

/**
 * Created with IntelliJ IDEA.
 * User: eprystupa
 * Date: 2/14/14
 * Time: 9:54 PM
 */
object SimpleClusterApp extends App {

  if (args.nonEmpty) System.setProperty("akka.remote.netty.tcp.port", args(0))

  val system = ActorSystem("ClusterSystem")
  val clusterListener = system.actorOf(Props[SimpleClusterListener], name = "clusterListener")

  Cluster(system).subscribe(clusterListener, classOf[ClusterDomainEvent])
}

class SimpleClusterListener extends Actor with ActorLogging {
  override def receive: Receive = {
    case state: CurrentClusterState ⇒
      log.info("Current members: {}", state.members.mkString(", "))
    case MemberUp(member) ⇒
      log.info("Member is Up: {}", member.address)
    case UnreachableMember(member) ⇒
      log.info("Member detected as unreachable: {}", member)
    case MemberRemoved(member, previousStatus) ⇒
      log.info("Member is Removed: {} after {}",
        member.address, previousStatus)
    case _: ClusterDomainEvent ⇒ // ignore
  }
}