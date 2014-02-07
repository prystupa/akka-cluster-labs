package com.prystupa

import akka.actor.{Props, Actor}

class HelloWorld extends Actor {

  override def preStart(): Unit = {
    // create the greeter actor
    val greeter = context.actorOf(Props[Greeter], "greeter")
    // send unsupported message
    greeter ! "Bye"
    // tell it to perform the greeting
    greeter ! Greeter.Greet
  }

  override def receive: Receive = {
    case Greeter.Done => context.stop(self)
  }
}
