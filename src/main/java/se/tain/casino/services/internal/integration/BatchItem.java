package se.tain.casino.services.internal.integration;

import java.io.Serializable;
import java.math.BigDecimal;

public class BatchItem implements Serializable {
    private String externalRoundId;
    private String referringRoundId;
    private String externalTransactionId;
    private BigDecimal amount;
    private GameTransferType transferType;
    private boolean bonusTransparent;

    public BatchItem( String externalRoundId, String referringRoundId, String externalTransactionId, BigDecimal amount, GameTransferType transferType, boolean bonusTransparent ) {
        this.externalTransactionId = externalTransactionId;
        this.referringRoundId = referringRoundId;
        this.amount = amount;
        this.transferType = transferType;
        this.externalRoundId = externalRoundId;
        this.bonusTransparent = bonusTransparent;
    }

    public String getExternalRoundId() {
        return externalRoundId;
    }

    public void setExternalRoundId( String externalRoundId ) {
        this.externalRoundId = externalRoundId;
    }

    public String getReferringRoundId() {
        return referringRoundId;
    }

    public void setReferringRoundId( String referringRoundId ) {
        this.referringRoundId = referringRoundId;
    }

    public String getExternalTransactionId() {
        return externalTransactionId;
    }

    public void setExternalTransactionId( String externalTransactionId ) {
        this.externalTransactionId = externalTransactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount( BigDecimal amount ) {
        this.amount = amount;
    }

    public GameTransferType getTransferType() {
        return transferType;
    }

    public void setTransferType( GameTransferType transferType ) {
        this.transferType = transferType;
    }

    public boolean isBonusTransparent() {
        return bonusTransparent;
    }

    public void setBonusTransparent( boolean bonusTransparent ) {
        this.bonusTransparent = bonusTransparent;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;

        BatchItem batchItem = ( BatchItem ) o;

        if ( bonusTransparent != batchItem.bonusTransparent ) return false;
        if ( amount != null ? !amount.equals( batchItem.amount ) : batchItem.amount != null ) return false;
        if ( externalRoundId != null ? !externalRoundId.equals( batchItem.externalRoundId ) : batchItem.externalRoundId != null )
            return false;
        if ( externalTransactionId != null ? !externalTransactionId.equals( batchItem.externalTransactionId ) : batchItem.externalTransactionId != null )
            return false;
        if ( referringRoundId != null ? !referringRoundId.equals( batchItem.referringRoundId ) : batchItem.referringRoundId != null )
            return false;
        if ( transferType != batchItem.transferType ) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = externalRoundId != null ? externalRoundId.hashCode() : 0;
        result = 31 * result + ( referringRoundId != null ? referringRoundId.hashCode() : 0 );
        result = 31 * result + ( externalTransactionId != null ? externalTransactionId.hashCode() : 0 );
        result = 31 * result + ( amount != null ? amount.hashCode() : 0 );
        result = 31 * result + ( transferType != null ? transferType.hashCode() : 0 );
        result = 31 * result + ( bonusTransparent ? 1 : 0 );
        return result;
    }
}
