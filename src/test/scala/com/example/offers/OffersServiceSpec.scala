package com.example.offers

import cats.effect.IO
import org.http4s._
import org.http4s.implicits._
import org.scalatest.{BeforeAndAfterEach, Matchers, WordSpec}

class OffersServiceSpec extends WordSpec with Matchers with BeforeAndAfterEach {

  override def beforeEach(): Unit = {
    MongoConnector.dropDatabase()
    MongoConnector.insert(Offer("1", "This is a test", 100, "GBP"))
  }

  override def afterEach(): Unit = {
    MongoConnector.dropDatabase()
  }

  "The Offers service" when {

    "it receives a query for an existing offer" should {

      lazy val returnOffer: Response[IO] = {
        val getOffer = Request[IO](Method.GET, Uri.uri("/1"))
        WebServer.offersService.orNotFound(getOffer).unsafeRunSync()
      }

      def uriReturns200() = returnOffer.status shouldBe Status.Ok

      def uriReturnsOffer() = {
        val expected = """{"_id":"1","desc":"This is a test","price":100,"currency":"GBP","hasExpired":false}"""
        returnOffer.as[String].unsafeRunSync() shouldBe expected
      }

      "return a 200 status" in {
        uriReturns200()
      }

      "return the offer with the corresponding ID" in {
        uriReturnsOffer()
      }

    }

    "it receives a query for a non-existent offer" should {

      lazy val returnOffer: Response[IO] = {
        val getOffer = Request[IO](Method.GET, Uri.uri("/4"))
        WebServer.offersService.orNotFound(getOffer).unsafeRunSync()
      }

      def uriReturns404() = returnOffer.status shouldBe Status.NotFound

      "return a 404 status" in {
        uriReturns404()
      }

    }

    "it receives a request to create a new offer" should {

      lazy val createOffer: Response[IO] = {
        val postOffer: Request[IO] = Request[IO](Method.POST, Uri.uri("/"))
          .withBody(Offer("2", "New offer", 200, "GBP")).unsafeRunSync()
        WebServer.offersService.orNotFound(postOffer).unsafeRunSync()
      }

      def uriReturns201() = createOffer.status shouldBe Status.Created

      def uriReturnsCreatedOffer() = {
        val expected = """{"_id":"2","desc":"New offer","price":200,"currency":"GBP","hasExpired":false}"""
        createOffer.as[String].unsafeRunSync() shouldBe expected
      }

      "return a 201 status" in {
        uriReturns201()
      }

      "return the created offer" in {
        uriReturnsCreatedOffer()
      }

    }

    "it receives a valid expire request" should {

      lazy val expireOffer: Response[IO] = {
        val patchOffer = Request[IO](Method.PATCH, Uri.uri("/1"))
          .withBody(ExpireCommand(true)).unsafeRunSync()
        WebServer.offersService.orNotFound(patchOffer).unsafeRunSync()
      }

      def uriReturns204() = expireOffer.status shouldBe Status.NoContent

      "return a 204 status" in {
        uriReturns204()
      }

    }

    "it receives an invalid expire request" should {

      lazy val expireOffer: Response[IO] = {
        val patchOffer = Request[IO](Method.PATCH, Uri.uri("/1"))
          .withBody(ExpireCommand(false)).unsafeRunSync()
        WebServer.offersService.orNotFound(patchOffer).unsafeRunSync()
      }

      def uriReturns204() = expireOffer.status shouldBe Status.BadRequest

      "return a 204 status" in {
        uriReturns204()
      }

    }

  }

}
