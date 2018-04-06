package com.example.offers

import cats.effect.IO
import io.circe.generic.auto._
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe._

case class ExpireCommand(expire: Boolean)

object ExpireCommand {
  implicit val jsonDecoder: EntityDecoder[IO, ExpireCommand] = jsonOf[IO, ExpireCommand]
  implicit val jsonEncoder: EntityEncoder[IO, ExpireCommand] = jsonEncoderOf[IO, ExpireCommand]
}
