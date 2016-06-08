package org.androidorm.exceptions;

/**
 * Basic exception for this library.
 */
public class AndroidOrmException extends Exception {

    private static final long serialVersionUID = -7270105774135584839L;

    /**
     * Constructor.
     *
     * @param msg       exception message
     * @param arguments arguments for formating
     */
    public AndroidOrmException(String msg, Object... arguments) {
        super(String.format(msg, arguments));
    }

    /**
     * Constructor.
     *
     * @param msg       exception message
     * @param cause     exception cause
     * @param arguments arguments for formating
     */
    public AndroidOrmException(String msg, Throwable cause, Object... arguments) {
        super(String.format(msg, arguments), cause);
    }
}
