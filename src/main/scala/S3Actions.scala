package com.automatedlabs.akka.aws

import scala.concurrent.duration.Duration
import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import collection.JavaConversions._
import scala.collection.mutable.Buffer
import scala.concurrent.duration._
import scala.concurrent.Future
import java.lang.UnsupportedOperationException

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.services.s3.model._

class S3ActionActor extends Actor with ActorLogging {

  import context.dispatcher

  implicit val timeout = Timeout(5.seconds)
  val clientCache = context.actorFor("../clientCache")

  def receive = {
    case cmd : S3Cmd => 
      try {
        log.info("recieved {}", cmd.getClass.getName)
        val fClient = (clientCache ? GetS3Client(cmd.credentials)).mapTo[AmazonS3Client]

        cmd match {
          case S3ListMyBucketsCmd(_) => 
            sender ! fClient.map { c => c.listBuckets }
          case S3ListBucketCmd(_, bucket: String) =>
            sender.tell( fClient.map { c => c.listObjects(bucket) })
          case _ =>
            throw new UnsupportedOperationException("command not yet implemented")
        }
      } catch {
        case e: Exception =>
          sender ! akka.actor.Status.Failure(e)
          throw e
      }
  }
}

trait S3Cmd extends AWSCommand

case class S3ListMyBucketsCmd(credentials: AWSCredentials) extends S3Cmd
case class S3ListBucketCmd(credentials: AWSCredentials, bucket: String) extends S3Cmd
case class S3GetObject(credentials: AWSCredentials, request: GetObjectRequest) extends S3Cmd
case class S3PutObject(credentials: AWSCredentials, request: PutObjectRequest) extends S3Cmd
