import java.net.InetAddress
import java.util
import java.util.UUID


import com.typesafe.config.{ Config, ConfigFactory }

import akka.NotUsed

import com.amazonaws.internal.StaticCredentialsProvider
import com.amazonaws.auth.{BasicAWSCredentials, AWSCredentialsProvider}
import com.amazonaws.services.kinesis.clientlibrary.interfaces.{IRecordProcessorCheckpointer, IRecordProcessor, IRecordProcessorFactory}
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.{Worker, KinesisClientLibConfiguration}
import com.amazonaws.services.kinesis.clientlibrary.types.{UserRecord}
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.ShutdownReason;
import com.amazonaws.services.kinesis.model.Record

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import com.amazonaws.auth.AWSCredentialsProviderChain
import com.amazonaws.services.kinesis.clientlibrary.lib.worker._
import com.contxt.kinesis.KinesisSource
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Random

object Main extends App{
  val config = ConfigFactory.load() 
  val accessKeyId = config.getString("accessKeyId")
  val secretAccessKey = config.getString("secretAccessKey")

  println(s"[stream-config] ${config.getString("accessKeyId")}")
  println(s"[stream-config] ${config.getString("secretAccessKey")}")

  val appName = "kinesis-test-app"
  val streamName = "akka-stream"

  val initialPosition = "LATEST"
  val region = "ap-northeast-1"
  val idleTimeBetweenReadsInMillis = 3000

  val workerId = InetAddress.getLocalHost.getCanonicalHostName + ":" + UUID.randomUUID

  val credentialsProvider: AWSCredentialsProvider = new StaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey))

  val consumerConfig = new KinesisClientLibConfiguration(
    appName,
    streamName,
    credentialsProvider,
    workerId
  )
    .withRegionName(region)
    .withCallProcessRecordsEvenForEmptyRecordList(true)
    .withInitialPositionInStream(InitialPositionInStream.TRIM_HORIZON)

  case class KeyMessage(key: String, message: String, markProcessed: () => Unit)

  val atLeastOnceSource = KinesisSource(consumerConfig)
    .map { kinesisRecord =>

      println(s"[stream] ああああああああ")
      KeyMessage(
        kinesisRecord.partitionKey, kinesisRecord.data.utf8String, kinesisRecord.markProcessed
      )
    }
    // Records may be processed out of order without affecting checkpointing.
    .grouped(10).map(batch => Random.shuffle(batch)).mapConcat(identity)
    .map { message =>
      // After a record is marked as processed, it is eligible to be checkpointed in DynamoDb.
      message.markProcessed()
      message
    }

  implicit val system = ActorSystem("Main")
  implicit val materializer = ActorMaterializer()
  atLeastOnceSource.runWith(Sink.foreach(println))

  Thread.sleep(100.seconds.toMillis)
  Await.result(system.terminate(), Duration.Inf)
}