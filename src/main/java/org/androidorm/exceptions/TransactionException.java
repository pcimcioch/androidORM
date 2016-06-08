package org.androidorm.exceptions;

/**
 * Transaction exception.
 */
public class TransactionException extends AndroidOrmException {

    private static final long serialVersionUID = -5814311953581218500L;

    /**
     * Constructor.
     *
     * @param msg       exception message
     * @param arguments arguments for formating
     */
    public TransactionException(String msg, Object... arguments) {
        super(msg, arguments);
    }
}