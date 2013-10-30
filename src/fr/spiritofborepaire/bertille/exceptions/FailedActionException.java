/**
 * 
 */
package fr.spiritofborepaire.bertille.exceptions;

/**
 * Exception generated where a action failed.
 * 
 */
public class FailedActionException extends RuntimeException {

    private static final long serialVersionUID = 6357004125889033460L;

    public FailedActionException() {
	super();
    }

    public FailedActionException(String detailMessage, Throwable throwable) {
	super(detailMessage, throwable);
    }

    public FailedActionException(String detailMessage) {
	super(detailMessage);
    }

    public FailedActionException(Throwable throwable) {
	super(throwable);
    }

}
