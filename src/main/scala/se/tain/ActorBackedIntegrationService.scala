package se.tain

import java.math.BigDecimal
import java.util

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import org.javamoney.moneta.Money
import se.tain.actors._
import se.tain.casino.services.internal.integration.TransferResult
import se.tain.casino.services.internal.integration._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class ActorBackedIntegrationService(system: ActorSystem) extends InternalCasinoService {
  implicit val timeout = Timeout(1 seconds)

  var playerIdCounter = 0L;
  var players = scala.collection.mutable.Map[String, ActorRef]()

  override def getPlayerDetails(playerName: String): PlayerDetails = {
    val player: ActorRef = resolvePlayerByName(playerName)
    val fPlayerInfo: Future[PlayerInfo] = ask(player, GetPlayerInfo).mapTo[PlayerInfo]
    remapPlayerInfo(Await.result(fPlayerInfo, timeout.duration))
  }

  override def getPlayerBalances(playerName: String, externalGameId: String): PlayerBalances = {
    val fBalances: Future[Map[Long,Money]] = resolvePlayerByName(playerName).ask(GetAllBalances).mapTo[Map[Long, Money]]
    val balances: Map[Long,Money] = Await.result(fBalances, timeout.duration)

    balances.map(_._2.getNumber.doubleValue).sum

    val playerBalances: PlayerBalances = new PlayerBalances
    playerBalances.setTotalBalance(BigDecimal.valueOf(balances.map(_._2.getNumber.doubleValue).sum))
    playerBalances.setCashBalance(BigDecimal.valueOf(balances.filter(_._1 == 0).map(_._2.getNumber.doubleValue).sum))
    playerBalances.setBonusBalance(BigDecimal.valueOf(balances.filter(_._1 != 0).map(_._2.getNumber.doubleValue).sum))
    playerBalances
  }

  override def closeGameRound(externalRoundId: String): Unit = {
    // we need player name here
  }

  override def lookupAccountTransfer(externalTransactionId: String): AccountTransfer = ???

  override def createBonusPayoutTransfer(externalTransactionId: String, amount: BigDecimal, playerName: String, bonusId: Integer): TransferResult = ???

  override def rollbackTransfer(playerName: String, transactionRef: String, txPostfix: String, closeGameRound: Boolean): TransferResult = ???

  override def getPlayerDetailsBySessionId(sessionId: String): PlayerDetails = {
    val player: ActorRef = resolvePlayerByName(s"player-s-${sessionId}")
    val fPlayerInfo: Future[PlayerInfo] = ask(player, GetPlayerInfo).mapTo[PlayerInfo]
    val playerInfo: PlayerInfo = Await.result(fPlayerInfo, timeout.duration)
    remapPlayerInfo(playerInfo)
  }

  override def exchangeToken(token: String): String = ???

  override def createGameTransfer(playerName: String, externalGameRef: String, externalRoundId: String, externalRefRoundId: String, freeGameOfferRef: String, bonusTransparent: Boolean, externalTransactionId: String, amount: BigDecimal, jackpotContributions: util.List[JackpotContributionData], transferType: GameTransferType, gameRoundFinished: Boolean): TransferResult = {
    val action = if(transferType == GameTransferType.BET) PlayerWithdraw(amount, externalTransactionId, externalGameRef, externalRoundId) else PlayerDeposit(amount, externalTransactionId, externalGameRef, externalRoundId)

    val fTransferResult: Future[ATransferResult] = resolvePlayerByName(playerName).ask(action).mapTo[ATransferResult]
    val transferResult: ATransferResult = Await.result(fTransferResult, timeout.duration)
    remapTransferResult(transferResult)
  }

  override def getPlayerDetailsByToken(token: String): PlayerDetails = ???

  override def createBatchGameTransfer(playerName: String, externalGameId: String, transferList: util.List[BatchItem], insufficientFundsBehaviour: InsufficientFundsBehaviour, gameRoundFinished: Boolean): util.List[TransferResult] = ???

  def remapTransferResult(transferResult: ATransferResult): TransferResult = {
    new TransferResult(
      transferResult.internalTxId,
      "",
      BigDecimal.valueOf(transferResult.balances.map(_.balance.getNumber.doubleValue).sum),
      BigDecimal.valueOf(transferResult.balances.filter(_.walletId == 0).map(_.balance.getNumber.doubleValue).sum),
      null,
      BigDecimal.valueOf(transferResult.balances.filter(_.walletId != 0).map(_.balance.getNumber.doubleValue).sum),
      false
    )
  }

  def remapPlayerInfo(p: PlayerInfo): PlayerDetails = {
    new PlayerDetails(0, p.name, p.currency, true)
  }

  // TODO: refactor to functional style
  def resolvePlayerByName(playerName: String):ActorRef = {
    val nextId = {val counter = playerIdCounter; ()=>{playerIdCounter+=1;counter}}
    lazy val pActor: ActorRef = system.actorOf(Player.props(nextId(), playerName, "EUR"), s"player-${playerName}")
    players.getOrElseUpdate(playerName, pActor)
  }

  // TODO: refactor to functional style
  def resolvePlayerBySession(playerName: String):ActorRef = {
    val nextId = {val counter = playerIdCounter; ()=>{playerIdCounter+=1;counter}}
    players.getOrElseUpdate(playerName, system.actorOf(Player.props(nextId(), playerName, "EUR"), s"player-${playerName}"))
  }
}
