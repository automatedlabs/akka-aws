package com.automatedlabs.akka.aws

import scala.concurrent.duration.Duration
import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import collection.JavaConversions._
import scala.collection.mutable.Buffer
import scala.concurrent.duration._
import java.lang.UnsupportedOperationException

import com.amazonaws.services.cloudwatch._
import com.amazonaws.auth.AWSCredentials

class CloudWatchActionActor extends Actor with akka.actor.ActorLogging {

  implicit val timeout = Timeout(5.seconds)

  def receive = {
    case cmd : CloudWatchCmd => 
      log.info("recieved {}", cmd.getClass.getName)
      val client = context.actorFor("../clientCache") ? GetCloudWatchClient(cmd.credentials)
      cmd match {
        case _ =>
          throw new UnsupportedOperationException("command not yet implemented")
      }
  }
}

trait CloudWatchCmd extends AWSCommand

