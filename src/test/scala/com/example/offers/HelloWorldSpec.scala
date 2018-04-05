package com.example.offers

import cats.effect.IO
import org.http4s._
import org.http4s.implicits._
import org.scalatest.{Matchers, WordSpec}

class HelloWorldSpec extends WordSpec with Matchers {
  "HelloWorld" should {
    "return 200" in {
      uriReturns200()
    }
    "return hello world" in {
      uriReturnsHelloWorld()
    }
  }

  private val retHelloWorld: Response[IO] = {
    val getHW = Request[IO](Method.GET, Uri.uri("/hello/world"))
    HelloWorldServer.service.orNotFound(getHW).unsafeRunSync()
  }

  private def uriReturns200() =
    retHelloWorld.status shouldBe Status.Ok

  private def uriReturnsHelloWorld() =
    retHelloWorld.as[String].unsafeRunSync() shouldBe """{"message":"Hello, world"}"""
}
