package org.androidorm.exceptions;

/**
 * Mapping exception.
 */
public class MappingException extends AndroidOrmException {

    private static final long serialVersionUID = 2558062540496963125L;

    /**
     * Constructor.
     *
     * @param msg       exception message
     * @param arguments arguments for formating
     */
    public MappingException(String msg, Object... arguments) {
        super(msg, arguments);
    }

}
