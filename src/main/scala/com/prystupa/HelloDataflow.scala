package com.prystupa

import akka.actor.Actor
import akka.dataflow._
import akka.event.Logging

/**
 * Created with IntelliJ IDEA.
 * User: eprystupa
 * Date: 2/10/14
 * Time: 10:07 PM
 */
class HelloDataflow extends Actor {
  val log = Logging(context.system, this)
  import context.dispatcher

  val r1 = flow {"Hello "}
  val r2 = flow {"dataflow"}
  flow {self ! r1() + r2()}

  override def receive: Receive = {
    case r: String => log.info(r)
    context.stop(self)
  }
}
