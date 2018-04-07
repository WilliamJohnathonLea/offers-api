package com.example.offers

import cats.effect.IO
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.result.UpdateResult
import org.mongodb.scala.{Completed, FindObservable, MongoClient, MongoCollection, MongoDatabase, Observer, SingleObservable}


object MongoConnector {

  private val mongoClient: MongoClient = MongoClient()
  private val codecRegistry = fromRegistries(fromProviders(classOf[Offer]), DEFAULT_CODEC_REGISTRY)

  private lazy val db: MongoDatabase = mongoClient.getDatabase("offers-api").withCodecRegistry(codecRegistry)
  private lazy val offersCollection: MongoCollection[Offer] = db.getCollection("offers")


  // Internal API used for testing
  private[offers] def dropDatabase(): Unit = {
    db.drop().subscribe(new Observer[Completed] {
      override def onError(e: Throwable): Unit = ()

      override def onComplete(): Unit = ()

      override def onNext(result: Completed): Unit = ()
    })
  }

  def insert(offer: Offer): IO[Offer] = {

    val observable: SingleObservable[Completed] = offersCollection.insertOne(offer)

    IO async[Offer] { cb =>
      observable.subscribe(new Observer[Completed] {
        override def onNext(result: Completed): Unit = cb(Right(offer))

        override def onError(e: Throwable): Unit = cb(Left(e))

        override def onComplete(): Unit = ()
      })
    }

  }

  def retrieve(id: String): IO[Option[Offer]] = {

    val observable: FindObservable[Offer] = offersCollection.find(equal("_id", id))

    IO async[Option[Offer]] { cb =>
      observable.subscribe(new Observer[Offer] {
        override def onError(e: Throwable): Unit = cb(Left(e))

        override def onComplete(): Unit = cb(Right(None))

        override def onNext(result: Offer): Unit = cb(Right(Some(result)))
      })
    }
  }

  def expire(id: String): IO[Unit] = {

    val observable: SingleObservable[UpdateResult] = offersCollection.updateOne(equal("_id", id), set("hasExpired", true))

    IO async { cb =>
      observable.subscribe(new Observer[UpdateResult] {
        override def onError(e: Throwable): Unit = cb(Left(e))

        override def onComplete(): Unit = ()

        override def onNext(result: UpdateResult): Unit = cb(Right())
      })
    }
  }

}
