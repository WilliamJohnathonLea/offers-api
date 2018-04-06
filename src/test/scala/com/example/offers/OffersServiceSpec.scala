package com.example.offers

import cats.effect.IO
import org.http4s._
import org.http4s.implicits._
import org.scalatest.{Matchers, WordSpec}

class OffersServiceSpec extends WordSpec with Matchers {
  "The Offers service" when {

    "it receives a query for an existing offer" should {

      "return a 200 status" in {
        uriReturns200()
      }
      "return the offer with the corresponding ID" in {
        uriReturnsOffer()
      }

    }

    "it receives a request to create a new offer" should {

      "return a 201 status" in {
        uriReturns201()
      }

      "return the created offer" in {
        uriReturnsCreatedOffer()
      }

    }
  }

  private val returnOffer: Response[IO] = {
    val getOffer = Request[IO](Method.GET, Uri.uri("/1"))
    WebServer.offersService.orNotFound(getOffer).unsafeRunSync()
  }

  private val createOffer: Response[IO] = {
    val postOffer: Request[IO] = Request[IO](Method.POST, Uri.uri("/"))
      .withBody(Offer("1", "New offer", 200, "GBP")).unsafeRunSync()
    WebServer.offersService.orNotFound(postOffer).unsafeRunSync()
  }

  private def uriReturns200() =
    returnOffer.status shouldBe Status.Ok

  private def uriReturns201() =
    createOffer.status shouldBe Status.Created

  private def uriReturnsOffer() = {
    val expected = """{"_id":"1","desc":"This is a test","price":100,"currency":"GBP"}"""
    returnOffer.as[String].unsafeRunSync() shouldBe expected
  }

  private def uriReturnsCreatedOffer() = {
    val expected = """{"_id":"1","desc":"New offer","price":200,"currency":"GBP"}"""
    createOffer.as[String].unsafeRunSync() shouldBe expected
  }

}
