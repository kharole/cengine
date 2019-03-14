package se.tain.casino.services.internal.integration;

import se.tain.casino.common.exceptions.LimitException;
import se.tain.casino.common.exceptions.NotEnoughCreditException;

import java.math.BigDecimal;
import java.util.List;

public interface InternalCasinoService {
    PlayerDetails getPlayerDetails( String playerName );

    PlayerDetails getPlayerDetailsBySessionId( String sessionId );

    PlayerDetails getPlayerDetailsByToken( String token );

    PlayerBalances getPlayerBalances( String playerName, String externalGameId );

    String exchangeToken( String token );

    void closeGameRound( String externalRoundId );

    TransferResult createGameTransfer( String playerName, String externalGameRef, String externalRoundId, String externalRefRoundId, String freeGameOfferRef, boolean bonusTransparent, String externalTransactionId, BigDecimal amount, List<JackpotContributionData> jackpotContributions, GameTransferType transferType,
                                       boolean gameRoundFinished ) throws NotEnoughCreditException, LimitException;

    TransferResult rollbackTransfer( String playerName, String transactionRef, String txPostfix, boolean closeGameRound ) throws NotEnoughCreditException;

    List<TransferResult> createBatchGameTransfer( String playerName, String externalGameId, List<BatchItem> transferList, InsufficientFundsBehaviour insufficientFundsBehaviour, boolean gameRoundFinished ) throws NotEnoughCreditException;

    TransferResult createBonusPayoutTransfer( String externalTransactionId, BigDecimal amount, String playerName, Integer bonusId ) throws NotEnoughCreditException;

    AccountTransfer lookupAccountTransfer( String externalTransactionId );

}
