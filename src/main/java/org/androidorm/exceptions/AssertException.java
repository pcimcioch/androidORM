package org.androidorm.exceptions;

/**
 * Assert exception.
 */
public class AssertException extends AndroidOrmException {

    private static final long serialVersionUID = 4981512527070641636L;

    /**
     * Constructor.
     *
     * @param msg       exception message
     * @param arguments arguments for formating
     */
    public AssertException(String msg, Object... arguments) {
        super(msg, arguments);
    }

}
