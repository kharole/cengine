package se.tain.scalike

import  scalikejdbc.async._
import  scala.concurrent._
import  scala.concurrent.duration._
import scala.concurrent._, duration._, ExecutionContext.Implicits.global

class PlayerTest extends org.scalatest.FunSuite {

  test("player creation") {
    // set up connection pool (that's all you need to do)
    AsyncConnectionPool.singleton("jdbc:postgresql://localhost:5432/akka", "postgres", "passwd123")

    // create a new record within a transaction
    val gang = AsyncDB.localTx { implicit tx =>
      for {
        aaa <- Player.create("aaa", Some(1L))
        bbb <- Player.create("bbb", Some(2L))
      } yield List(aaa, bbb)
    }

    Await.result(gang, 5.seconds)
  }

}
