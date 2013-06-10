package com.automatedlabs.akka.aws

import akka.util.Timeout
import akka.actor.{Props, ActorLogging, Actor, OneForOneStrategy}
import akka.actor.SupervisorStrategy._

import scala.concurrent.duration._

class AWSSupervisor extends Actor with ActorLogging {

  import AWSCommand._

  implicit val timeout = Timeout(5.seconds)

  val clientCache =     context.actorOf(Props[AWSClientCacheActor], "clientCache")
  val s3Actor =         context.actorOf(Props(new S3ActionActor(clientCache)), "s3")
  val cloudwatchActor = context.actorOf(Props(new CloudWatchActionActor(clientCache)), "cloudwatch")

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
