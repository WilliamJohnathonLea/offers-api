package com.example.offers

import org.scalatest.{BeforeAndAfterEach, Matchers, WordSpec}

class MongoConnectorSpec extends WordSpec with Matchers with BeforeAndAfterEach {

  override def beforeEach(): Unit = {
    MongoConnector.dropDatabase()
  }

  "MongoConnector" should {

    "insert an Offer" in {

      val expected = Offer("1", "This is a test", 100, "GBP")

      MongoConnector.insert(expected).unsafeRunSync() shouldBe expected
    }

    "retrieve an offer" in {

      val offer = Offer("1", "This is a test", 100, "GBP")
      val expected = Some(offer)

      val result = for {
        o <- MongoConnector.insert(offer)
        res <- MongoConnector.retrieve(o._id)
      } yield res

      result.unsafeRunSync() shouldBe expected
    }

    "expire an offer" in {

      val offer = Offer("1", "This is a test", 100, "GBP")
      val expected: Option[Offer] = Some(offer.copy(hasExpired = true))

      val result = for {
        o <- MongoConnector.insert(offer)
        _ <- MongoConnector.expire(o._id)
        res <- MongoConnector.retrieve(o._id)
      } yield res

      result.unsafeRunSync() shouldBe expected
    }

  }

}
