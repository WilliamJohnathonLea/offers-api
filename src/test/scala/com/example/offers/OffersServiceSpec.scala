package com.example.offers

import cats.effect.IO
import org.http4s._
import org.http4s.implicits._
import org.scalatest.{BeforeAndAfterEach, Matchers, WordSpec}

class OffersServiceSpec extends WordSpec with Matchers with BeforeAndAfterEach {

  override def beforeEach(): Unit = {

    val setup = for {
      _ <- MongoConnector.dropDatabase()
      _ <- MongoConnector.insertWithIdAndDate(CreateOffer("This is a test", 100, "GBP", 86400), "1", 123456789)
    } yield ()

    setup.unsafeRunSync()
  }

  override def afterEach(): Unit = {
    MongoConnector.dropDatabase().unsafeRunSync()
  }

  "The Offers service" when {

    "it receives a query for an existing offer" should {

      lazy val returnOffer: Response[IO] = {
        val getOffer = Request[IO](Method.GET, Uri.uri("/1"))
        WebServer.offersService.orNotFound(getOffer).unsafeRunSync()
      }

      def uriReturns200() = returnOffer.status shouldBe Status.Ok

      def uriReturnsOffer() = {
        val expected = Offer("1", "This is a test", 100, "GBP", 123456789, 86400)
        returnOffer.as[Offer].unsafeRunSync() shouldBe expected
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
          .withBody(CreateOffer("New offer", 200, "GBP", 86400)).unsafeRunSync()
        WebServer.offersService.orNotFound(postOffer).unsafeRunSync()
      }

      def uriReturns201() = createOffer.status shouldBe Status.Created

      def uriReturnsCreatedOffer() = {
        val expectedDesc = "New offer"
        val expectedPrice = 200
        val expectedCurrency = "GBP"
        val expectedExpiresAfter = 86400
        val outputOffer = createOffer.as[Offer].unsafeRunSync()

        outputOffer.desc shouldBe expectedDesc
        outputOffer.price shouldBe expectedPrice
        outputOffer.currency shouldBe expectedCurrency
        outputOffer.expiresAfter shouldBe expectedExpiresAfter
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

      def uriReturns400() = expireOffer.status shouldBe Status.BadRequest

      "return a 400 status" in {
        uriReturns400()
      }

    }

  }

}
