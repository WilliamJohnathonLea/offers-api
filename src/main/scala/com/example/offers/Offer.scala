package com.example.offers

import cats.effect.IO
import io.circe.generic.auto._
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe._

case class Offer(
                  _id: String,
                  desc: String,
                  price: Int,
                  currency: String,
                  dateCreated: Long,
                  expiresAfter: Long,
                  hasExpired: Boolean = false)

object Offer {
  implicit val jsonDecoder: EntityDecoder[IO, Offer] = jsonOf[IO, Offer]
  implicit val jsonEncoder: EntityEncoder[IO, Offer] = jsonEncoderOf[IO, Offer]

  def fromCreateOffer(createOffer: CreateOffer, id: String, dateCreated: Long): Offer = {
    Offer(
      _id = id,
      desc = createOffer.desc,
      price = createOffer.price,
      currency = createOffer.currency,
      dateCreated = dateCreated,
      expiresAfter = createOffer.expiresAfter
    )
  }

}
