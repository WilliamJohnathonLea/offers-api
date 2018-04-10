package com.example.offers

import org.scalatest.{BeforeAndAfterEach, Matchers, WordSpec}

class MongoConnectorSpec extends WordSpec with Matchers with BeforeAndAfterEach {

  override def beforeEach(): Unit = {
    MongoConnector.dropDatabase().unsafeRunSync()
  }

  override def afterEach(): Unit = {
    MongoConnector.dropDatabase().unsafeRunSync()
  }

  "MongoConnector" should {

    "insert an Offer" in {

      val offer = CreateOffer("This is a test", 100, "GBP", 86400)

      val expectedDesc = "This is a test"
      val expectedPrice = 100
      val expectedCurrency = "GBP"
      val expectedExpiresAfter = 86400

      val outputOffer = MongoConnector.insert(offer).unsafeRunSync()

      outputOffer.desc shouldBe expectedDesc
      outputOffer.price shouldBe expectedPrice
      outputOffer.currency shouldBe expectedCurrency
      outputOffer.expiresAfter shouldBe expectedExpiresAfter
    }

    "retrieve an offer" in {

      val offer = CreateOffer("This is a test", 100, "GBP", 86400)

      val expectedDesc = "This is a test"
      val expectedPrice = 100
      val expectedCurrency = "GBP"
      val expectedExpiresAfter = 86400

      val result = for {
        o <- MongoConnector.insert(offer)
        res <- MongoConnector.retrieve(o._id)
      } yield res

      val outputOffer = result.unsafeRunSync().getOrElse(fail("Offer was not found"))

      outputOffer.desc shouldBe expectedDesc
      outputOffer.price shouldBe expectedPrice
      outputOffer.currency shouldBe expectedCurrency
      outputOffer.expiresAfter shouldBe expectedExpiresAfter
    }

    "expire an offer" in {

      val offer = CreateOffer("This is a test", 100, "GBP", 86400)

      val result = for {
        o <- MongoConnector.insert(offer)
        _ <- MongoConnector.expire(o._id)
        res <- MongoConnector.retrieve(o._id)
      } yield res

      result.unsafeRunSync().exists(_.hasExpired == true) shouldBe true
    }

  }

}
