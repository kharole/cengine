package se.tain.casino.services.internal.integration;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccountTransfer implements Serializable {
    private long id;
    private BigDecimal amount;
    private AccountTransferType type;
    private String playerName;
    private String externalId;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount( BigDecimal amount ) {
        this.amount = amount;
    }

    public AccountTransferType getType() {
        return type;
    }

    public void setType( AccountTransferType type ) {
        this.type = type;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName( String playerName ) {
        this.playerName = playerName;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId( String externalId ) {
        this.externalId = externalId;
    }
}
