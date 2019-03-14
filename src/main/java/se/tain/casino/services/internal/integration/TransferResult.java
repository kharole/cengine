package se.tain.casino.services.internal.integration;

import java.io.Serializable;
import java.math.BigDecimal;

public class TransferResult implements Serializable {

    private static final long serialVersionUID = 8434444706385636581L;

    private String transactionId;
    private String externalTransactionId;
    // TODO: use PlayerBalances instead of Credits/Amounts etc.
    private BigDecimal newPlayerCredits;
    private BigDecimal newPlayerCashAmount;
    private BigDecimal amount;
    private BigDecimal bonusAmount;
    private boolean alreadyProcessed;

    public TransferResult() {
    }

    public TransferResult( String transactionId, String externalTransactionId, BigDecimal newPlayerCredits, BigDecimal newPlayerCashAmount, BigDecimal amount, BigDecimal bonusAmount, boolean alreadyProcessed ) {
        this.transactionId = transactionId;
        this.externalTransactionId = externalTransactionId;
        this.newPlayerCredits = newPlayerCredits;
        this.newPlayerCashAmount = newPlayerCashAmount;
        this.amount = amount;
        this.bonusAmount = bonusAmount;
        this.alreadyProcessed = alreadyProcessed;
    }

    public String getExternalTransactionId() {
        return externalTransactionId;
    }

    public void setExternalTransactionId( String externalTransactionId ) {
        this.externalTransactionId = externalTransactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId( String transactionId ) {
        this.transactionId = transactionId;
    }

    public BigDecimal getNewPlayerCredits() {
        return newPlayerCredits;
    }

    public void setNewPlayerCredits( BigDecimal newPlayerCredits ) {
        this.newPlayerCredits = newPlayerCredits;
    }

    public BigDecimal getNewPlayerCashAmount() {
        return newPlayerCashAmount;
    }

    public void setNewPlayerCashAmount( BigDecimal newPlayerCashAmount ) {
        this.newPlayerCashAmount = newPlayerCashAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount( BigDecimal amount ) {
        this.amount = amount;
    }

    public BigDecimal getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount( BigDecimal bonusAmount ) {
        this.bonusAmount = bonusAmount;
    }

    public boolean isAlreadyProcessed() {
        return alreadyProcessed;
    }

    public void setAlreadyProcessed( boolean alreadyProcessed ) {
        this.alreadyProcessed = alreadyProcessed;
    }

    public boolean isFake() {
        return transactionId.endsWith( "FAKE" );
    }
}
