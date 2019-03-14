package se.tain.casino.common.exceptions;


public class CasinoCheckedException extends Exception {
    private static final long serialVersionUID = 1L;

    public CasinoCheckedException() {
    }

    public CasinoCheckedException( String message) {
        super(message);
    }

    public CasinoCheckedException( Throwable cause) {
        super(cause);
    }

    public CasinoCheckedException( String message, Throwable cause) {
        super(message, cause);
    }
}