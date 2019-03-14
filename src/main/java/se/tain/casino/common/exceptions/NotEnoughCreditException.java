package se.tain.casino.common.exceptions;

/**
 * Created May 7, 2005
 * 
 * @author Anders Hansson, Diabol consultant
 */
public class NotEnoughCreditException extends CasinoCheckedException {

	private static final long serialVersionUID = 8317292816147306281L;

	public NotEnoughCreditException() {
        super();
    }

    /**
     * @param message
     */
    public NotEnoughCreditException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public NotEnoughCreditException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public NotEnoughCreditException(String message, Throwable cause) {
        super(message, cause);
    }

}