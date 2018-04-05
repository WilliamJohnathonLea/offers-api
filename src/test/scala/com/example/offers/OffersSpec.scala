package com.example.offers

import cats.effect.IO
import org.http4s._
import org.http4s.implicits._
import org.scalatest.{Matchers, WordSpec}

class OffersSpec extends WordSpec with Matchers {
  "Offers" should {
    "return 200" in {
      uriReturns200()
    }
    "return hello world" in {
      uriReturnsHelloWorld()
    }
  }

  private val retHelloWorld: Response[IO] = {
    val getHW = Request[IO](Method.GET, Uri.uri("/world"))
    WebServer.offersService.orNotFound(getHW).unsafeRunSync()
  }

  private def uriReturns200() =
    retHelloWorld.status shouldBe Status.Ok

  private def uriReturnsHelloWorld() =
    retHelloWorld.as[String].unsafeRunSync() shouldBe """{"message":"Hello, world"}"""
}
