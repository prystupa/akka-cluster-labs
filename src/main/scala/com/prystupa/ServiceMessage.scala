package com.prystupa

import akka.actor.ActorRef

/**
 * Created with IntelliJ IDEA.
 * User: eprystupa
 * Date: 2/15/14
 * Time: 8:33 AM
 */

case object DiscoverServicesRequest

case class ServiceRegistration(service: ActorRef, metadata: String)