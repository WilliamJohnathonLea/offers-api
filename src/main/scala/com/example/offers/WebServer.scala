package com.example.offers

import cats.effect.IO
import fs2.StreamApp
import io.circe._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeBuilder
import scala.concurrent.ExecutionContext.Implicits.global

object WebServer extends StreamApp[IO] with Http4sDsl[IO] {
  val offersService: HttpService[IO] = HttpService[IO] {
    case GET -> Root / id =>
      Ok(
        Json.obj(
          "_id" -> Json.fromString(id),
          "desc" -> Json.fromString("This is a test"),
          "price" -> Json.fromInt(100),
          "currency" -> Json.fromString("GBP")
        )
      )
  }

  def stream(args: List[String], requestShutdown: IO[Unit]) =
    BlazeBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .mountService(offersService, "/api/offers")
      .serve
}
