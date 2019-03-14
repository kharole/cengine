package se.tain.casino.common.exceptions;

import se.tain.casino.common.limit.LimitType;

public class LimitException extends CasinoRuntimeException {
    private static final long serialVersionUID = -6540971077030557966L;

    private String playerName;
    private LimitType type;
    private Object limitValue;
    private Object actualValue;

    // Notify WinMasters before changing error message here since it is used in integration.
    public LimitException( String playerName, LimitType type, Object limitValue, Object actualValue ) {
        super( String.format( "%s limit exceeded for player %s. Progress so far %s >= Limit value %s",
                type, playerName, actualValue, limitValue ) );
        this.playerName = playerName;
        this.type = type;
        this.limitValue = limitValue;
        this.actualValue = actualValue;
    }

    public String getPlayerName() {
        return playerName;
    }

    public LimitType getType() {
        return type;
    }

    public Object getLimitValue() {
        return limitValue;
    }

    public Object getActualValue() {
        return actualValue;
    }
}
