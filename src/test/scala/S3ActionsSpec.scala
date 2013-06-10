package com.automatedlabs.akka.aws

import scala.concurrent.duration._
import scala.util.{Try, Success, Failure}

import akka.util.Timeout
import akka.actor.ActorSystem
import akka.testkit.TestKit
import akka.testkit.TestActorRef
import akka.testkit.ImplicitSender
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.BeforeAndAfterAll

class S3ActionSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
with FunSpec with ShouldMatchers with BeforeAndAfterAll {

  //import context.dispatcher

  def this() = this(ActorSystem("s3-action-spec"))

  implicit val timeout = Timeout(5.seconds)

  override def afterAll {
    system.shutdown()
  }

  import TestData._

  describe("An S3 action actor") {
    it ("should work") (pending)
  }
}
