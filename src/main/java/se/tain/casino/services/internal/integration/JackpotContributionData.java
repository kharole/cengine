package se.tain.casino.services.internal.integration;

import java.io.Serializable;
import java.math.BigDecimal;

public class JackpotContributionData implements Serializable {
    private static final long serialVersionUID = 3174910684433703681L;

    private String jackpotRef;
    private BigDecimal amount;

    public JackpotContributionData( String jackpotRef, BigDecimal amount ) {
        this.jackpotRef = jackpotRef;
        this.amount = amount;
    }

    public String getJackpotRef() {
        return jackpotRef;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;

        JackpotContributionData that = ( JackpotContributionData ) o;

        if ( amount != null ? !amount.equals( that.amount ) : that.amount != null ) return false;
        if ( jackpotRef != null ? !jackpotRef.equals( that.jackpotRef ) : that.jackpotRef != null ) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = jackpotRef != null ? jackpotRef.hashCode() : 0;
        result = 31 * result + ( amount != null ? amount.hashCode() : 0 );
        return result;
    }

    @Override
    public String toString() {
        return "JackpotContributionData{" +
                "jackpotRef='" + jackpotRef + '\'' +
                ", amount=" + amount +
                '}';
    }
}
