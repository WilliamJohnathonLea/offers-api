package com.example.offers

import cats.effect.IO
import fs2.StreamApp
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeBuilder

import scala.concurrent.ExecutionContext.Implicits.global

object WebServer extends StreamApp[IO] with Http4sDsl[IO] {
  val offersService: HttpService[IO] = HttpService[IO] {
    case req@POST -> Root =>
      val result = for {
        offerIn <- req.as[Offer]
        offerOut <- MongoConnector.insert(offerIn)
      } yield offerOut.asJson
      Created(result)
    case GET -> Root / id =>
      val result = for {
        optOffer <- MongoConnector.retrieve(id)
      } yield optOffer match {
        case Some(offer) => Ok(offer.asJson)
        case None => NotFound()
      }
      result.unsafeRunSync()
    case req@PATCH -> Root / id =>
      val result = for {
        command <- req.as[ExpireCommand]
        _ <- if(command.expire) MongoConnector.expire(id) else IO.pure()
      } yield if(command.expire) NoContent() else BadRequest()
      result.unsafeRunSync()
  }

  def stream(args: List[String], requestShutdown: IO[Unit]) =
    BlazeBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .mountService(offersService, "/api/offers")
      .serve
}
