package com.automatedlabs.akka.aws

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy._

import scala.concurrent.duration._

import com.amazonaws.auth.AWSCredentials

class AWSSupervisor extends Actor with akka.actor.ActorLogging {

  implicit val timeout = Timeout(5.seconds)

  val clientCache = context.actorOf(Props[AWSClientCacheActor], "clientCache")
  val s3Actor = context.actorOf(Props[S3ActionActor], "s3")
  val cloudwatchActor = context.actorOf(Props[CloudWatchActionActor], "cloudwatch")

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1.minute) {
      case _: ArithmeticException      => Resume
      case _: NullPointerException     => Restart
      case _: IllegalArgumentException => Stop
      case _: Exception                => Escalate
    }
 
  def receive = {
    case cmd: S3Cmd => s3Actor forward cmd 
    case cmd: CloudWatchCmd => cloudwatchActor forward cmd
  }

}

trait AWSCommand {
  def credentials: AWSCredentials
}




