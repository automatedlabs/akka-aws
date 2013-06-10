package com.automatedlabs.akka.aws

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import java.lang.UnsupportedOperationException

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model._

class S3ActionActor(clientCache: ActorRef) extends Actor with ActorLogging {

  import AWSCommand._
  //import context.dispatcher

  implicit val timeout = Timeout(5.seconds)

  override def receive: Receive = {
    case cmd: S3Cmd =>
      try {
        log.info("recieved {}", cmd.getClass.getName)
        (clientCache ? GetS3Client(cmd.credentials)).mapTo[AmazonS3Client].map {
          c: AmazonS3Client =>

            cmd match {
              // Bucket operations
              case S3ListMyBuckets(_) =>
                sender ! c.listBuckets
              case S3ListBucket(_, bucket: String) =>
                sender ! c.listObjects(bucket)
              case S3DeleteBucket(_, bucket: String) =>
                sender ! c.deleteBucket(bucket)
              // Object operations
              case S3GetObject(_, request: GetObjectRequest) =>
                sender ! c.getObject(request)
              case S3PutObject(_, request: PutObjectRequest) =>
                sender ! c.putObject(request)
              case S3DeleteObject(_, request: DeleteObjectRequest) =>
                sender ! c.deleteObject(request)
              case default =>
                throw new UnsupportedOperationException(s"command not yet implemented: ${default}")
            }
        }
      } catch {
        case e: Exception =>
          sender ! akka.actor.Status.Failure(e)
          throw e
      }
  }
}

