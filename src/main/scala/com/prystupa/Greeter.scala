package com.prystupa

import akka.actor.Actor

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

  override def receive: Receive = {
    case Greeter.Greet =>
      println("Hello World")
      sender ! Greeter.Done
  }
}
