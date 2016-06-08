package org.androidorm.exceptions;

/**
 * Reflection exception.
 */
public class ReflectionException extends AndroidOrmException {

    private static final long serialVersionUID = -5480124057023404466L;

    /**
     * Constructor.
     *
     * @param msg       exception message
     * @param cause     exception cause
     * @param arguments arguments for formating
     */
    public ReflectionException(String msg, Throwable cause, Object... arguments) {
        super(msg, cause, arguments);
    }
}
