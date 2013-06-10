package com.automatedlabs.akka.aws

import akka.testkit.TestActorRef
import com.amazonaws.auth.{AnonymousAWSCredentials, BasicAWSCredentials}

object TestData {
  val cacheActorRef = TestActorRef[AWSClientCacheActor]
  val cacheActor = cacheActorRef.underlyingActor

  val credentials = new BasicAWSCredentials("AAAAA", "BBBBB")
  val credentials2 = new BasicAWSCredentials("ZZZZZ", "YYYYY")
  val anonCredentials = new AnonymousAWSCredentials()
}
