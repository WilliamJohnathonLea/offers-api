package com.example.offers

import cats.effect.IO

object MongoConnector {

  def insert(offer: Offer): IO[Offer] = {
    IO(offer)
  }

  def retrieve(id: String): IO[Offer] = {
    IO(Offer(id, "This is a test", 100, "GBP"))
  }

}
