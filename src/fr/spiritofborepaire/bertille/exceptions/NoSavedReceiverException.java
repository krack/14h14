/**
 * 
 */
package fr.spiritofborepaire.bertille.exceptions;

/**
 * Exception generated where a receiver use is not saved
 * 
 */
public class NoSavedReceiverException extends RuntimeException {

    private static final long serialVersionUID = 6357004125889033460L;

    public NoSavedReceiverException() {
	super();
    }

    public NoSavedReceiverException(String detailMessage, Throwable throwable) {
	super(detailMessage, throwable);
    }

    public NoSavedReceiverException(String detailMessage) {
	super(detailMessage);
    }

    public NoSavedReceiverException(Throwable throwable) {
	super(throwable);
    }

}
