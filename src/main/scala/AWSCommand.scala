package com.automatedlabs.akka.aws

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.services.s3.model.{DeleteObjectRequest, PutObjectRequest, GetObjectRequest}


trait AWSCommand {
  def credentials: AWSCredentials
}

object AWSCommand {

  sealed trait S3Cmd extends AWSCommand

  sealed trait CloudWatchCmd extends AWSCommand

  case class S3ListMyBuckets(credentials: AWSCredentials) extends S3Cmd

  case class S3ListBucket(credentials: AWSCredentials, bucket: String) extends S3Cmd

  case class S3DeleteBucket(credentials: AWSCredentials, bucket: String) extends S3Cmd

  case class S3GetObject(credentials: AWSCredentials, request: GetObjectRequest) extends S3Cmd

  //case class S3GetObjectByKey(credentials: AWSCredentials, bucket: String, key: String) extends S3Cmd

  case class S3PutObject(credentials: AWSCredentials, request: PutObjectRequest) extends S3Cmd

  //case class S3PutObjectByKey(credentials: AWSCredentials, bucket: String, key: String, file: File) extends S3Cmd

  case class S3DeleteObject(credentials: AWSCredentials, request: DeleteObjectRequest) extends S3Cmd

  //case class S3DeleteObjectByKey(credentials: AWSCredentials, bucket: String, key: String) extends S3Cmd

}
