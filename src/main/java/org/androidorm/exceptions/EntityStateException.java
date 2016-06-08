package org.androidorm.exceptions;

/**
 * Entity state exception.
 */
public class EntityStateException extends AndroidOrmException {

    private static final long serialVersionUID = -6889805403252178303L;

    /**
     * Constructor.
     *
     * @param msg       exception message
     * @param arguments arguments for formating
     */
    public EntityStateException(String msg, Object... arguments) {
        super(msg, arguments);
    }

}
