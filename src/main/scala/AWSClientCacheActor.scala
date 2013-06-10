package com.automatedlabs.akka.aws

import scala.concurrent.duration._
import scala.collection.mutable

import akka.actor._
import akka.util.Timeout

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.AmazonWebServiceClient

class AWSClientCacheActor extends Actor with ActorLogging {

  implicit val timeout = Timeout(1.seconds)

  val clientCache = new mutable.HashMap[String, mutable.HashMap[
                          Class[_ <: AmazonWebServiceClient], AmazonWebServiceClient]] 

  def receive = {
    case req : GetAWSClient =>
      sender ! clientCache.
        getOrElseUpdate(req.credentials.getAWSAccessKeyId, 
                        new mutable.HashMap[Class[_ <: AmazonWebServiceClient], AmazonWebServiceClient]).
        getOrElseUpdate(req.clientType, req.createClient(req.credentials))
  }

}

sealed trait GetAWSClient {
  type T <: AmazonWebServiceClient // abstract type
  def tM : Manifest[T]
  def credentials : AWSCredentials
  def clientType : Class[T] = tM.erasure.asInstanceOf[Class[T]]
  def createClient(creds: AWSCredentials) : T
}

case class GetS3Client(credentials: AWSCredentials) extends GetAWSClient {
  import com.amazonaws.services.s3.AmazonS3Client
  type T = AmazonS3Client
  val tM = manifest[T]
  def createClient(creds: AWSCredentials) = new AmazonS3Client(creds)
}

case class GetCloudWatchClient(credentials: AWSCredentials) extends GetAWSClient {
  import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsyncClient
  type T = AmazonCloudWatchAsyncClient
  val tM = manifest[T]
  def createClient(creds: AWSCredentials) = new AmazonCloudWatchAsyncClient(creds)
}
