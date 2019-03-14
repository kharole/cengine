package se.tain.casino.common.exceptions;

public class CasinoRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CasinoRuntimeException() {
    }

    public CasinoRuntimeException( String message ) {
        super( message );
    }

    public CasinoRuntimeException( Throwable cause ) {
        super( cause );
    }

    public CasinoRuntimeException( String message, Throwable cause ) {
        super( message, cause );
    }
}