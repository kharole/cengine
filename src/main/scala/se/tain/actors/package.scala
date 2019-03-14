package se.tain

import org.javamoney.moneta.Money
import org.joda.time.DateTime

import scala.concurrent.duration.Duration

package object actors {
  case object Ok
  // player actions
  case object Login
  case object Logout
  case class PlayerInfo(id:Long, name:String, currency:String)

  // wallet operations
  case object GetPlayerInfo
  case object GetBalance
  case object GetAllBalances
  case class Balance(walletId:Long, balance: Money)
  case class PlayerDeposit(amount:BigDecimal,extTxId:String,gameId:String = null,extRoundId:String = null)
  case class PlayerWithdraw(amount:BigDecimal,extTxId:String,gameId:String = null,extRoundId:String = null)
  case class Deposit(amount:Money,extTxId:String,gameId:String = null,extRoundId:String = null)
  case class Withdraw(amount:Money,extTxId:String,gameId:String = null,extRoundId:String = null)
  case class AddWallet(walletId:Long, balance:Money, startDate:DateTime, lifeTime:Duration)

  case class ATransferResult(internalTxId:String, balances:List[Balance], alreadyProcessed:Boolean)

  // exceptions
  class NotEnoughCredits(message: String, cause: Throwable = null) extends RuntimeException(message, cause)
  class PlayerLoggedOut(message: String, cause: Throwable = null) extends RuntimeException(message, cause)
  class WrongCurrency(message: String, cause: Throwable = null) extends RuntimeException(message, cause)
}
