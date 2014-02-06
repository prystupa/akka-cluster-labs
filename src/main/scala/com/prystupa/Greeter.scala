package com.prystupa

import akka.actor.Actor
import akka.event.Logging

/**
 * Created with IntelliJ IDEA.
 * User: eprystupa
 * Date: 2/4/14
 * Time: 9:31 PM
 */

object Greeter {

  case object Greet

  case object Done

}

class Greeter extends Actor {
  val log = Logging(context.system, this)

  override def receive: Receive = {
    case Greeter.Greet =>
      log.info("Hello World")
      sender ! Greeter.Done
  }
}
