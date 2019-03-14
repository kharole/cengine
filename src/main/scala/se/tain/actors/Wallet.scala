package se.tain.actors

import akka.actor.{ActorRef, Actor, Props}
import akka.persistence.PersistentActor
import org.javamoney.moneta.Money
import org.joda.time.DateTime
import se.tain.actors.GetBalance

object Wallet {
  def props(id: Long, balance: Money) = Props(classOf[Wallet], id, balance, DateTime.now, 24)
}

case class Deposited(amount: Money, extTxId: String, gameId: String = null, extRoundId: String = null)

case class Withdrawn(amount: Money, extTxId: String, gameId: String = null, extRoundId: String = null)

/*There should be wallet provider and life time*/
class Wallet(id: Long, var balance: Money, startDate: DateTime, expirationHours: Int) extends PersistentActor {

  override def receiveRecover: Receive = {
    case GetBalance =>
    case Deposit(amount, txId, gameId, roundId) => balance = balance.add(amount)
    case Withdraw(amount, txId, gameId, roundId) => balance = balance.subtract(amount)
  }

  def updateDepositState(sender: ActorRef)(event: Deposited): Unit = {
    balance = balance.add(event.amount)
    sender ! Balance(id, balance)
  }

  def updateWithdrawState(sender: ActorRef)(event: Withdrawn): Unit = {
    balance = balance.subtract(event.amount)
    sender ! Balance(id, balance)
  }

  override def receiveCommand: Receive = {
    case GetBalance => sender() ! Balance(id, balance)
    case Deposit(amount, txId, gameId, roundId) => {
      val s = sender()
      persist(Deposited(amount, txId, gameId, roundId))(updateDepositState(s))
    }
    case Withdraw(amount, txId, gameId, roundId) => {
      //if (balance.compareTo(amount) < 0) throw new NotEnoughCredits(s"Not enough credits in wallet[${id}:${balance}}] to make a bet of ${amount}")
      val s = sender()
      persist(Withdrawn(amount, txId, gameId, roundId))(updateWithdrawState(s))
    }
  }

  override def persistenceId: String = "wallet-persistence-id"
}
