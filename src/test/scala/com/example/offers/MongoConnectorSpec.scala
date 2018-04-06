package com.example.offers

import cats.effect.IO
import org.scalatest.{Matchers, WordSpec}

class MongoConnectorSpec extends WordSpec with Matchers {

  "MongoConnector" should {

    "insert an Offer" in {

      val offer = Offer("1", "This is a test", 100, "GBP")
      val expected = IO(offer)

      MongoConnector.insert(offer) shouldBe expected
    }

    "retrieve an offer" in {

      val offer = Offer("1", "This is a test", 100, "GBP")
      val expected = IO(offer)

      MongoConnector.retrieve("1") shouldBe expected
    }

  }

}
