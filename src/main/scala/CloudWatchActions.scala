package com.automatedlabs.akka.aws

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import java.lang.UnsupportedOperationException

class CloudWatchActionActor(clientCache: ActorRef) extends Actor with akka.actor.ActorLogging {

  import AWSCommand._
  implicit val timeout = Timeout(5.seconds)

  def receive = {
    case cmd : CloudWatchCmd => 
      log.info("recieved {}", cmd.getClass.getName)
      val client = clientCache ? GetCloudWatchClient(cmd.credentials)
      cmd match {
        case _ =>
          throw new UnsupportedOperationException("command not yet implemented")
      }
  }
}



