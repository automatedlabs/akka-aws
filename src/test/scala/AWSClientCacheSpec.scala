package com.automatedlabs.akka.aws

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.util.{Try, Success, Failure}
import scala.collection.mutable.HashMap

import akka.util.Timeout
import akka.actor._
import akka.pattern.ask
import akka.testkit._
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.BeforeAndAfterAll

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsyncClient

class AWSClientCacheSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
with FunSpec with ShouldMatchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("aws-client-cache-spec"))

  implicit val timeout = Timeout(5.seconds)

  import TestData._

  override def afterAll {
    system.shutdown()
  }

  describe("An AWSClientCache actor") {

    it("starts with an empty cache") {
      cacheActor.clientCache should be('empty)
    }

    describe("provides S3 clients") {
      describe("using the credentials 'AAAAA'") {
        it("should accept a GetS3Client message") {
          cacheActorRef ! GetS3Client(credentials)
          expectMsgClass(200.millis, classOf[AmazonS3Client])
        }

        it("should then have one item in the cache") {
          cacheActor.clientCache should have size (1)
        }

        it("should have the access key as the hashmap key") {
          cacheActor.clientCache should contain key ("AAAAA")
        }

        it("should accept an identical GetS3Client message") {
          cacheActorRef ! GetS3Client(credentials)
          expectMsgClass(200.millis, classOf[AmazonS3Client])
        }

        it("should still only have one item in the cache") {
          cacheActor.clientCache should have size (1)
        }

        describe("two requests for the same credentials") {
          it("should return the same client object") {
            val f1 = cacheActorRef ? GetS3Client(credentials)
            val f2 = cacheActorRef ? GetS3Client(credentials)
            val c1 = Await.result(f1, 1.second).asInstanceOf[AmazonS3Client]
            val c2 = Await.result(f2, 1.second).asInstanceOf[AmazonS3Client]
            c1 should be theSameInstanceAs (c2)
          }
        }

        describe("the cache entry for 'AAAAA'") {
          it("should have two client entries in it") {
            cacheActor.clientCache.get("AAAAA").map {
              _ should have size (1)
            }
          }
        }

      }
    }

    describe("provides CloudWatch clients and") {
      describe("using the credentials 'AAAAA'") {
        it("should accept a GetCloudWatchClient message") {
          cacheActorRef ! GetCloudWatchClient(credentials)
          expectMsgClass(200.millis, classOf[AmazonCloudWatchAsyncClient])
        }

        it("should still have one item in the cache") {
          cacheActor.clientCache should have size (1)
        }

        describe("the cache entry for 'AAAAA'") {
          it("should have two client entries in it") {
            cacheActor.clientCache.get("AAAAA").map {
              _ should have size (2)
            }
          }
        }
      }
      describe("using the credentials 'ZZZZZ'") {
        it("should accept a GetCloudWatchMessage") {
          cacheActorRef ! GetCloudWatchClient(credentials2)
          expectMsgClass(200.millis, classOf[AmazonCloudWatchAsyncClient])
        }

        it("should have two items in the cache") {
          cacheActor.clientCache should have size (2)
        }

        describe("the cache entry for 'ZZZZZ'") {
          it("should have one client entry in it") {
            cacheActor.clientCache.get("ZZZZZ").map {
              _ should have size (1)
            }
          }
        }
      }

    }

    describe("when using AnonymousAWSCredentials") {
      it("should accept a request")(pending)
    }
  }
}
