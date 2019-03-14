package se.tain.actors.wallet

import akka.actor.{ActorSystem, Props}
import akka.persistence.jdbc.common.PluginConfig
import akka.persistence.jdbc.extension.ScalikeExtension
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.typesafe.config.ConfigFactory
import org.javamoney.moneta.Money
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Matchers, WordSpecLike}
import scalikejdbc.DBSession
import se.tain.actors._
import se.tain.actors.util.{GenericJdbcInit, PostgresqlJdbcInit}

import scala.concurrent.duration._


class WalletTest extends TestKit(ActorSystem("WalletTest", ConfigFactory.load("application.conf")))
with Matchers
with WordSpecLike
with BeforeAndAfterAll
with BeforeAndAfterEach
with ImplicitSender
with GenericJdbcInit
with PostgresqlJdbcInit {

  override def afterAll: Unit =
    system.terminate()

  override protected def beforeAll(): Unit = {
    //dropJournalTable()
    //createJournalTable()
    //dropSnapshotTable()
    //createSnapshotTable()
    super.beforeAll()
  }

  override protected def beforeEach(): Unit = {
    //clearJournalTable()
    //clearSnapshotTable()
    super.beforeEach()
  }

  val wallet = system.actorOf(Wallet.props(0, zero("EUR")))

  val cfg = PluginConfig(system)
  override implicit val session: DBSession = ScalikeExtension(system).session

  def zero(currency:String) = Money.of(0, currency)
  def money(amount:BigDecimal, currency:String) = Money.of(amount, currency)

  "Wallet Actor" should {
    "initialize with zero balance" in {
        wallet ! GetBalance
        expectMsg(Balance(0, zero("EUR")))
    }

    "persist balance changes" in {
      wallet ! Deposit(money(10, "EUR"), "AT_1")
      expectMsg(Balance(0, money(10, "EUR")))

      // TODO: this should be idempotent call
      wallet ! Deposit(money(5, "EUR"), "AT_1")
      expectMsg(Balance(0, money(15, "EUR")))

      // TODO: this should be idempotent call
      wallet ! Deposit(money(2, "EUR"), "AT_2")
      expectMsg(Balance(0, money(17, "EUR")))

      wallet ! Withdraw(money(11, "EUR"), "AT_3")
      expectMsg(Balance(0, money(6, "EUR")))
    }

    "throw exception when not enough funds" in {
      val props: Props = Wallet.props(1, money(1, "EUR"))

      val zeroWallet = TestActorRef.create(system, props, "WalletActor")
      intercept[NotEnoughCredits] { zeroWallet receive Withdraw(money(2, "EUR"), "AT_1") }
    }
  }


}

object WalletTest {
  val config =
    """
      |akka {
      |      loglevel = "DEBUG"
      |    }
    """.stripMargin
}
