package test.com.automatedlabs.akka.aws

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.util.{Try, Success, Failure}
import scala.collection.mutable.HashMap

import akka.util.Timeout
import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.pattern.ask
import akka.testkit.TestKit
import akka.testkit.TestActorRef
import akka.testkit.ImplicitSender
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.BeforeAndAfterAll

import com.automatedlabs.akka.aws._
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.AnonymousAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client


class TestS3ActionSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
with FunSpec with ShouldMatchers with BeforeAndAfterAll {

  //import context.dispatcher

  def this() = this(ActorSystem("AWSSupervisor"))

  implicit val timeout = Timeout(5.seconds)

  override def afterAll {
    system.shutdown()
  }

  val actionActorRef = TestActorRef[AWSSupervisor]
  val actionActor = actionActorRef.underlyingActor
  val credentials = new BasicAWSCredentials("AAAAA", "BBBBB")

  describe("An S3 action actor") {
    it ("should work") (pending)
  }
}
