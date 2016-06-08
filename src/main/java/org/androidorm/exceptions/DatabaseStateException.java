package org.androidorm.exceptions;

/**
 * Database state exception.
 */
public class DatabaseStateException extends AndroidOrmException {

    private static final long serialVersionUID = -980371814952801079L;

    /**
     * Constructor.
     *
     * @param msg       exception message
     * @param arguments arguments for formating
     */
    public DatabaseStateException(String msg, Object... arguments) {
        super(msg, arguments);
    }

    /**
     * Constructor.
     *
     * @param msg       exception message
     * @param cause     exception cause
     * @param arguments arguments for formating
     */
    public DatabaseStateException(String msg, Throwable cause, Object... arguments) {
        super(msg, cause, arguments);
    }
}
