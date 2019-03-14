package se.tain.casino.services.internal.integration;

import java.io.Serializable;
import java.math.BigDecimal;

public class PlayerBalances implements Serializable {

    private static final long serialVersionUID = 6899822997418592249L;

    private BigDecimal cashBalance;
    private BigDecimal bonusBalance;
    private BigDecimal totalBalance;

    public BigDecimal getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance( BigDecimal cashBalance ) {
        this.cashBalance = cashBalance;
    }

    public BigDecimal getBonusBalance() {
        return bonusBalance;
    }

    public void setBonusBalance( BigDecimal bonusBalance ) {
        this.bonusBalance = bonusBalance;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance( BigDecimal totalBalance ) {
        this.totalBalance = totalBalance;
    }

}
