package se.tain.actors

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.typesafe.config.ConfigFactory
import org.javamoney.moneta.Money
import org.joda.time.DateTime
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._

class PlayerTest extends TestKit(ActorSystem("PlayerActorTest", ConfigFactory.parseString(PlayerTest.config)))
with Matchers
with WordSpecLike
with BeforeAndAfterAll
with ImplicitSender {

  override def afterAll: Unit = system.terminate()

  val player = system.actorOf(Player.props(0, "test", "EUR"))

  def zero(currency:String) = Money.of(0, currency)
  def money(amount:BigDecimal, currency:String) = Money.of(amount, currency)

  "Player Actor" should {
    "process deposit" in {
        player ! PlayerDeposit(10.0, "AT_1")
        expectMsg(ATransferResult("AT_0_0", List(Balance(0, money(10, "EUR"))), false))

        within(10 millis) {
          player ! GetBalance
          expectMsg(money(10, "EUR"))
        }
    }

    "get bonuses" in {
      val player = system.actorOf(Player.props(0, "test", "EUR"))

      player ! PlayerDeposit(5.0, "AT_1")
      expectMsg(ATransferResult("AT_0_0", List(Balance(0, money(5, "EUR"))), false))

      player ! AddWallet(1, money(10, "EUR"), DateTime.now, 1 hour)
      expectMsg(Ok)

      player ! GetBalance
      expectMsg(money(15, "EUR"))
    }

    "payout respects bet ratios for bonuses" in {
      val player = system.actorOf(Player.props(0, "test", "EUR"))

      player ! PlayerDeposit(2.0, "AT_1")
      expectMsg(ATransferResult("AT_0_0", List(Balance(0, money(2, "EUR"))), false))
      player ! AddWallet(1, money(10, "EUR"), DateTime.now, 1 hour)
      expectMsg(Ok)

      player ! GetBalance
      expectMsg(money(12, "EUR"))

      player ! PlayerWithdraw(10.0, "NET_1_1", "NET_starburst", "NET_1")
      expectMsg(ATransferResult("GT_0_1", List(Balance(0, money(0, "EUR")), Balance(1, money(2, "EUR"))), false))

      player ! PlayerDeposit(20.0, "NET_1_1", "NET_starburst", "NET_1")
      expectMsg(ATransferResult("GT_0_2", List(Balance(0, money(4, "EUR")), Balance(1, money(18, "EUR"))), false))
    }
  }
}

object PlayerTest {
  val config =
    """
      |akka {
      |  loglevel = "DEBUG"
      |}
    """.stripMargin
}