package com.example.offers

import org.scalatest.{Matchers, WordSpec}

class MongoConnectorSpec extends WordSpec with Matchers {

  "MongoConnector" should {

    "insert an Offer" in {

      val expected = Offer("1", "This is a test", 100, "GBP")

      MongoConnector.insert(expected).unsafeRunSync() shouldBe expected
    }

    "retrieve an offer" in {

      val expected = Offer("1", "This is a test", 100, "GBP")

      MongoConnector.retrieve("1").unsafeRunSync() shouldBe expected
    }

  }

}
