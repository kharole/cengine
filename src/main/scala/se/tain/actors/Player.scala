package se.tain.actors

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import org.javamoney.moneta.Money

import scala.annotation.tailrec
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object Player {
  def props(id:Long, name:String, currency:String) = Props(classOf[Player], id, name, currency)
}

class Player(id:Long, name: String, currency: String) extends Actor {
  var txCounter = 0

  var openedRoundsCache: collection.mutable.Map[String, List[(Long, Money)]] = null
  var balancesCache: collection.mutable.Map[Long, Money] = null

  var cashWallet: ActorRef = null
  var bonusWallets: ListBuffer[ActorRef] = ListBuffer()

  var walletIndexes: collection.mutable.Map[Long, ActorRef] = null

  override def preStart() = {
    // TODO: get all wallets from db. Init Wallet Actors with proper state(ie balance, ids etc)
    cashWallet = context.actorOf(Wallet.props(0, Money.of(0, currency)))

    walletIndexes = collection.mutable.Map(0L -> cashWallet)
    balancesCache = collection.mutable.Map(getBalances: _*)

    openedRoundsCache = collection.mutable.Map()
  }

  import scala.concurrent.ExecutionContext.Implicits.global

  override def receive: Receive = {
    case GetAllBalances => sender() ! collection.immutable.Map(balancesCache.toList: _*)
    case GetBalance => sender ! collectBalance
    case GetPlayerInfo => sender() ! PlayerInfo(id, name, currency)
    case AddWallet(id, balance, _, _) => {
      // TODO: we should not set balance to wallets. Deposit call should follow right after AddWallet
      val walletRef: ActorRef = context.actorOf(Wallet.props(id, balance))
      bonusWallets += walletRef
      walletIndexes(id) = walletRef
      balancesCache(id) = balance

      // TODO: we should return balance here
      sender ! Ok
    }
    case PlayerDeposit(decimalAmount, extTxId, gameId, extRoundId) => {
      implicit val timeout = Timeout(3 seconds)

      val amount: Money = Money.of(decimalAmount.doubleValue(), currency)
      val depositsAmounts: Map[Long, Money] = calculateDepositAmounts(extRoundId, amount, openedRoundsCache.getOrElse(extRoundId, List()))

      val fDeposits: List[Future[Balance]] = depositsAmounts.map { case (wId, wAm) => walletIndexes(wId).ask(Deposit(wAm, extTxId, gameId, extRoundId)).mapTo[Balance] }.toList
      val txId = generateInternalTxId(gameId)
      val s = sender()
      Future.sequence(fDeposits).map(ATransferResult(txId, _, false)) onComplete {
        case scala.util.Success(result:ATransferResult) => {
          result.balances.foreach { case Balance(wId, wBal) => balancesCache(wId) = wBal }

          if (openedRoundsCache.contains(extRoundId)) openedRoundsCache -= extRoundId

          s ! result
        }
        case scala.util.Failure(_) => /*do something*/
      }

    }
    case PlayerWithdraw(decimalAmount, extTxId, gameId, extRoundId) => {
      implicit val timeout = Timeout(3 seconds)

      val amount: Money = Money.of(decimalAmount.doubleValue(), currency)

      if (decimalAmount > collectBalance.getNumber.doubleValue()) throw new NotEnoughCredits(s"Player ${this.name} has not enough credits to make a bet of ${amount}")
      val withdrawAmounts: Map[Long, Money] = calculateWithdrawAmounts(amount)
      val fWithdraws: List[Future[Balance]] = withdrawAmounts.map { case (wId, wAm) => walletIndexes(wId).ask(Withdraw(wAm, extTxId, gameId, extRoundId)).mapTo[Balance] }.toList

      val s = sender()
      Future.sequence(fWithdraws).map(ATransferResult(generateInternalTxId(gameId), _, false)) onComplete {
        case scala.util.Success(result:ATransferResult) => {
          result.balances.foreach { case Balance(wId, wBal) => balancesCache(wId) = wBal }

          openedRoundsCache(extRoundId) = withdrawAmounts.toList

          s ! result
        }
        case scala.util.Failure(_) => /*do something*/
      }
    }
  }

  def generateInternalTxId(gameId:String):String = {
    val nextId = {val counter = txCounter; ()=>{txCounter+=1;counter}}
    s"${if(gameId==null)'A'else'G'}T_${id}_${nextId()}"
  }

  def calculateWithdrawAmounts(amount: Money): Map[Long, Money] = {
    @tailrec
    def iter(moneyLeft: Money, result: Map[Long, Money]): Map[Long, Money] = {
      if (moneyLeft isZero) return result

      val walletsToWithdraw: mutable.Map[Long, Money] = balancesCache.filter { case (walletId, _) => !result.contains(walletId) }
      if (walletsToWithdraw.isEmpty) throw new NotEnoughCredits("Player has not enough credits in his wallets")

      // TODO: obviously this sorting function is useless. There should be more taken into account
      val firstWalletBalance: (Long, Money) = walletsToWithdraw.toSeq.sortBy(_._1).head
      val withdrawAmount = if (firstWalletBalance._2.compareTo(moneyLeft) < 0) firstWalletBalance._2 else moneyLeft

      iter(moneyLeft.subtract(withdrawAmount), result + (firstWalletBalance._1 -> withdrawAmount))
    }

    iter(amount, Map[Long, Money]())
  }

  def calculateDepositAmounts(extRoundId: String, amount:Money, withdrawals:List[(Long,Money)]): Map[Long, Money] = {
    if(withdrawals isEmpty)
      Map(0L -> amount)
    else {
      val totalBet = withdrawals.map(_._2).foldLeft(Money.of(0, currency))(_ add _)
      val betRatios: Map[Long, Double] = withdrawals.toMap.map { case(wId, wAm) => wId -> wAm.getNumber.doubleValueExact()/totalBet.getNumber.doubleValueExact() }

      betRatios.toMap.map { case(wId, ratio) => wId -> amount.multiply(ratio)}
    }
  }

  def collectBalance = {
    balancesCache.map(_._2).reduce(_ add _)
  }

  def getBalances = {
    implicit val timeout = Timeout(3 seconds)

    val fBalances: List[Future[Balance]] = (cashWallet :: bonusWallets.toList).map(_.ask(GetBalance).mapTo[Balance])
    val balances: List[Balance] = Await.result(Future.sequence(fBalances), 3 seconds)
    balances.map(b => b.walletId -> b.balance)
  }

}
