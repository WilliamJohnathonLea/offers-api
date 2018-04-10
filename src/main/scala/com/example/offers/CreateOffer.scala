package com.example.offers

import cats.effect.IO
import io.circe.generic.auto._
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe._

case class CreateOffer(desc: String, price: Int, currency: String, expiresAfter: Long)

object CreateOffer {
  implicit val jsonDecoder: EntityDecoder[IO, CreateOffer] = jsonOf[IO, CreateOffer]
  implicit val jsonEncoder: EntityEncoder[IO, CreateOffer] = jsonEncoderOf[IO, CreateOffer]
}
